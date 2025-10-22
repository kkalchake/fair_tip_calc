#!/usr/bin/env python3
"""
Bill Splitter - Production-quality CLI for splitting restaurant bills fairly.
Uses Decimal for precise money calculations. Python 3.10+ required.
"""

from decimal import Decimal, ROUND_HALF_UP, getcontext
from typing import Any
import sys

# Set high precision for intermediate calculations
getcontext().prec = 28


def print_welcome() -> None:
    """Display welcome banner."""
    print("\n" + "=" * 60)
    print("  🍽️  BILL SPLITTER")
    print("=" * 60 + "\n")


def read_money(prompt: str) -> Decimal:
    """
    Read a monetary amount from user input.
    Re-prompts on invalid input. Accepts zero or positive values.
    """
    while True:
        user_input = input(prompt).strip()
        if not user_input:
            print("Input cannot be empty. Please enter a number.")
            continue
        try:
            value = Decimal(user_input)
            if value < 0:
                print("Amount cannot be negative. Please try again.")
                continue
            return value.quantize(Decimal("0.01"), rounding=ROUND_HALF_UP)
        except Exception:
            print("Invalid number. Please enter a valid amount.")


def read_percentage(prompt: str) -> Decimal:
    """
    Read a percentage value (e.g., 15 for 15%).
    Re-prompts on invalid input. Accepts zero or positive values.
    """
    while True:
        user_input = input(prompt).strip()
        if not user_input:
            print("Input cannot be empty. Please enter a percentage.")
            continue
        try:
            value = Decimal(user_input)
            if value < 0:
                print("Percentage cannot be negative. Please try again.")
                continue
            return value
        except Exception:
            print("Invalid percentage. Please enter a valid number.")


def read_nonempty_str(prompt: str) -> str:
    """
    Read a non-empty string from user.
    Strips whitespace and re-prompts if empty.
    """
    while True:
        value = input(prompt).strip()
        if value:
            return value
        print("Input cannot be empty. Please try again.")


def read_yes_no(prompt: str) -> bool:
    """
    Ask a yes/no question. Returns True for yes, False for no.
    Re-prompts on invalid input.
    """
    while True:
        answer = input(prompt + " (yes/no): ").strip().lower()
        if answer in ["yes", "y"]:
            return True
        elif answer in ["no", "n"]:
            return False
        print("Please answer 'yes' or 'no'.")


def read_int_min(prompt: str, min_value: int) -> int:
    """
    Read an integer from user with minimum value constraint.
    Re-prompts on invalid input.
    """
    while True:
        user_input = input(prompt).strip()
        if not user_input:
            print("Input cannot be empty. Please enter a number.")
            continue
        try:
            value = int(user_input)
            if value < min_value:
                print(f"Must be at least {min_value}. Please try again.")
                continue
            return value
        except ValueError:
            print("Invalid number. Please enter a whole number.")


def parse_items_csv(prompt: str) -> list[Decimal]:
    """
    Parse comma-separated monetary amounts.
    Returns empty list if input is empty (person has no individual items).
    Re-prompts on malformed values.
    """
    while True:
        user_input = input(prompt).strip()
        if not user_input:
            return []
        
        items: list[Decimal] = []
        parts = user_input.split(",")
        
        try:
            for part in parts:
                stripped = part.strip()
                if stripped:
                    amount = Decimal(stripped)
                    if amount < 0:
                        print("Amounts cannot be negative. Please try again.")
                        raise ValueError
                    items.append(amount.quantize(Decimal("0.01"), rounding=ROUND_HALF_UP))
            return items
        except Exception:
            print("Invalid format. Enter comma-separated amounts (e.g., 12.50, 7.25).")


def format_money(x: Decimal) -> str:
    """Format a Decimal as currency string (e.g., $12.34)."""
    quantized = x.quantize(Decimal("0.01"), rounding=ROUND_HALF_UP)
    return f"${quantized}"


def compute_tip_from_percent(bill: Decimal, percent: Decimal) -> Decimal:
    """Calculate tip amount from bill and percentage."""
    return (bill * percent / Decimal("100")).quantize(Decimal("0.01"), rounding=ROUND_HALF_UP)


def collect_people(n: int) -> list[dict[str, Any]]:
    """
    Collect information for n people.
    Each person: name (unique, non-empty), items (list of Decimal), subtotal, shared (default 0).
    """
    people: list[dict[str, Any]] = []
    existing_names: set[str] = set()
    
    print("\n" + "-" * 60)
    print("STEP 5: Individual Orders")
    print("For each person, enter items as comma-separated amounts.")
    print("Example: 12.50, 8.99, 15.00")
    print("Leave blank if no individual items.\n")
    
    for i in range(n):
        print(f"Person {i + 1}:")
        
        # Get unique name
        while True:
            name = read_nonempty_str("  Name: ")
            if name.lower() in existing_names:
                print("  Name already used. Please enter a different name.")
                continue
            existing_names.add(name.lower())
            break
        
        # Get items
        items = parse_items_csv("  Items (comma-separated amounts): ")
        subtotal = sum(items, Decimal("0"))
        
        person: dict[str, Any] = {
            "name": name,
            "items": items,
            "subtotal": subtotal,
            "shared": Decimal("0")
        }
        people.append(person)
        
        print(f"  Subtotal: {format_money(subtotal)}\n")
    
    return people


def handle_shared_items(people: list[dict[str, Any]]) -> None:
    """
    Handle shared items if any.
    Mutates people list by adding to each person's 'shared' total.
    """
    print("\n" + "-" * 60)
    print("STEP 6: Shared Items")
    print("-" * 60 + "\n")
    
    has_shared = read_yes_no("Were any items shared between people?")
    
    if not has_shared:
        return
    
    num_shared = read_int_min("How many items were shared? ", 1)
    
    # Build name lookup (case-insensitive)
    name_map: dict[str, dict[str, Any]] = {p["name"].lower(): p for p in people}
    
    print()
    for i in range(num_shared):
        print(f"Shared Item {i + 1}:")
        
        amount = read_money("  Amount: $")
        
        # Get valid sharers
        while True:
            names_input = input("  Who shared this? (comma-separated names): ").strip()
            if not names_input:
                print("  Input cannot be empty.")
                continue
            
            names = [n.strip() for n in names_input.split(",")]
            
            if len(names) < 2:
                print("  At least 2 people must share an item.")
                continue
            
            # Validate all names exist
            sharers: list[dict[str, Any]] = []
            all_valid = True
            
            for name in names:
                if not name:
                    continue
                name_lower = name.lower()
                if name_lower not in name_map:
                    print(f"  '{name}' not found. Check spelling!")
                    all_valid = False
                    break
                sharers.append(name_map[name_lower])
            
            if not all_valid or len(sharers) < 2:
                continue
            
            break
        
        # Split amount among sharers
        split_amount = amount / Decimal(len(sharers))
        
        for sharer in sharers:
            sharer["shared"] += split_amount
        
        print(f"  Each person pays: {format_money(split_amount)}\n")


def compute_tax_tip_share(tax: Decimal, tip: Decimal, n: int) -> Decimal:
    """Calculate per-person share of tax and tip."""
    return (tax + tip) / Decimal(n)


def render_bill_summary(bill: Decimal, tax: Decimal, tip: Decimal, n: int) -> None:
    """Display bill summary."""
    total = bill + tax + tip
    
    print("\n" + "=" * 60)
    print("FINAL RESULTS")
    print("=" * 60 + "\n")
    print("Bill Summary:")
    print(f"  Bill (before tax): {format_money(bill)}")
    print(f"  Tax:               {format_money(tax)}")
    print(f"  Tip:               {format_money(tip)}")
    print(f"  Total:             {format_money(total)}")
    print(f"  Split among:       {n} people")
    print()


def render_person_breakdown(people: list[dict[str, Any]], tax_tip_share: Decimal) -> None:
    """Display breakdown for each person."""
    print("Individual Breakdowns:")
    print("-" * 60)
    
    for person in people:
        print(f"\n{person['name']}:")
        
        # Individual items
        if person["items"]:
            items_str = ", ".join(format_money(item) for item in person["items"])
            print(f"  Individual items: {items_str}")
        print(f"  Individual subtotal: {format_money(person['subtotal'])}")
        
        # Shared items
        print(f"  Shared items total:  {format_money(person['shared'])}")
        
        # Tax + tip share
        print(f"  Tax + Tip share:     {format_money(tax_tip_share)}")
        
        # Total
        total_owed = person["subtotal"] + person["shared"] + tax_tip_share
        print(f"  {'-' * 30}")
        print(f"  TOTAL OWED:          {format_money(total_owed)}")


def main() -> None:
    """Main orchestration function."""
    print_welcome()
    
    # STEP 1: Bill amount
    print("STEP 1: Bill Amount")
    print("-" * 60)
    bill = read_money("Enter bill amount (before tax): $")
    
    # STEP 2: Tax
    print("\n" + "-" * 60)
    print("STEP 2: Tax")
    print("-" * 60)
    tax = read_money("Enter tax amount: $")
    
    # STEP 3: Tip
    print("\n" + "-" * 60)
    print("STEP 3: Tip")
    print("-" * 60)
    
    tip_included = read_yes_no("Is tip already included in the bill?")
    tip = Decimal("0")
    
    if tip_included:
        tip = read_money("How much is the tip? $")
    else:
        want_tip = read_yes_no("Would you like to add a tip?")
        if want_tip:
            tip_percent = read_percentage("Enter tip percentage (e.g., 18 for 18%): ")
            tip = compute_tip_from_percent(bill, tip_percent)
            print(f"Tip amount: {format_money(tip)}")
            
            if tip_percent > Decimal("20"):
                print("\n🌟 Thank you for your generosity! 🌟")
    
    # STEP 4: Number of people
    print("\n" + "-" * 60)
    print("STEP 4: Number of People")
    print("-" * 60)
    num_people = read_int_min("How many people are splitting? ", 2)
    
    # STEP 5: Collect people and orders
    people = collect_people(num_people)
    
    # STEP 6: Handle shared items
    handle_shared_items(people)
    
    # Calculate and display results
    tax_tip_share = compute_tax_tip_share(tax, tip, num_people)
    render_bill_summary(bill, tax, tip, num_people)
    render_person_breakdown(people, tax_tip_share)
    
    print("\n" + "=" * 60)
    print("Thank you! 😊\n")


def run_demo_calculations() -> None:
    """
    Demonstrate key calculations without user input.
    Run with: python bill_splitter.py --demo
    """
    print("\n" + "=" * 60)
    print("DEMO MODE - Testing Key Calculations")
    print("=" * 60 + "\n")
    
    # Test 1: Tip percent > 20 (generosity message)
    print("Test 1: Tip > 20% (should show generosity)")
    bill1 = Decimal("100.00")
    tip_percent1 = Decimal("25")
    tip1 = compute_tip_from_percent(bill1, tip_percent1)
    print(f"  Bill: {format_money(bill1)}, Tip %: {tip_percent1}%, Tip: {format_money(tip1)}")
    if tip_percent1 > Decimal("20"):
        print("  🌟 Thank you for your generosity! 🌟")
    
    # Test 2: Included tip path
    print("\nTest 2: Tip already included")
    bill2 = Decimal("150.00")
    tip2 = Decimal("30.00")
    print(f"  Bill: {format_money(bill2)}, Included Tip: {format_money(tip2)}")
    
    # Test 3: No tip path
    print("\nTest 3: No tip")
    bill3 = Decimal("80.00")
    tip3 = Decimal("0")
    print(f"  Bill: {format_money(bill3)}, Tip: {format_money(tip3)}")
    
    # Test 4: Shared item split
    print("\nTest 4: Shared item calculation")
    shared_amount = Decimal("24.00")
    num_sharers = 2
    split = shared_amount / Decimal(num_sharers)
    print(f"  Shared amount: {format_money(shared_amount)}")
    print(f"  Split among: {num_sharers} people")
    print(f"  Each pays: {format_money(split)}")
    
    # Test 5: Tax + Tip split
    print("\nTest 5: Tax + Tip per-person calculation")
    tax5 = Decimal("15.00")
    tip5 = Decimal("37.50")
    num_people5 = 3
    share = compute_tax_tip_share(tax5, tip5, num_people5)
    print(f"  Tax: {format_money(tax5)}, Tip: {format_money(tip5)}")
    print(f"  People: {num_people5}")
    print(f"  Per person: {format_money(share)}")
    
    # Test 6: Decimal precision
    print("\nTest 6: Decimal precision (8.875 / 2)")
    tax6 = Decimal("8.875")
    num_people6 = 2
    share6 = (tax6 + Decimal("0")) / Decimal(num_people6)
    print(f"  Tax: {format_money(tax6)}, People: {num_people6}")
    print(f"  Per person (exact): {share6}")
    print(f"  Per person (formatted): {format_money(share6)}")
    
    print("\n" + "=" * 60)
    print("Demo complete!\n")


if __name__ == "__main__":
    if len(sys.argv) > 1 and sys.argv[1] == "--demo":
        run_demo_calculations()
    else:
        main()
