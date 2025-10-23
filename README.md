# fair_tip_calc
Alternative for unfair splitting math is ready to help you here https://kkalchake.github.io/fair_tip_calc/ - With Nice Front End

# 🍽️ Bill Splitter

A production-quality command-line application for splitting restaurant bills fairly among multiple people. Built with Python 3.10+ using `Decimal` for precise money calculations.

## Features

✨ **Accurate Money Calculations** - Uses Python's `Decimal` type to avoid floating-point errors  
💰 **Flexible Tip Options** - Handle pre-included tips or calculate new ones  
🤝 **Shared Items Support** - Fairly split appetizers, desserts, or any shared dishes  
🎯 **Smart Validation** - Robust input validation with helpful error messages  
👥 **Multi-Person Splitting** - Split bills among 2 or more people  
🌟 **Generosity Recognition** - Special message for tips over 20%  

## Requirements

- Python 3.10 or higher
- No external dependencies (uses standard library only)

## Installation

1. **Download the file:**
```bash
   curl -O https://raw.githubusercontent.com/yourusername/bill-splitter/main/bill_splitter.py
```

2. **Make it executable (optional):**
```bash
   chmod +x bill_splitter.py
```

## Usage

### Run the application:
```bash
python3 bill_splitter.py
```

### Run demo mode (test calculations):
```bash
python3 bill_splitter.py --demo
```

## How It Works

The application guides you through a simple step-by-step process:

### **Step 1: Bill Amount**
Enter the total bill amount before tax.

### **Step 2: Tax**
Enter the tax amount.

### **Step 3: Tip**
Choose one of three options:
- Tip already included (enter the amount)
- Add a new tip (enter percentage)
- No tip

*If you tip more than 20%, you'll receive a special thank you message! 🌟*

### **Step 4: Number of People**
Enter how many people are splitting the bill (minimum 2).

### **Step 5: Individual Orders**
For each person:
- Enter their name
- Enter items as comma-separated amounts (e.g., `12.50, 8.99, 15.00`)
- Leave blank if they had no individual items

### **Step 6: Shared Items (Optional)**
If any items were shared:
- Specify how many shared items
- For each shared item:
  - Enter the amount
  - Enter names of people who shared it (comma-separated)

### **Results**
The app displays:
- Complete bill summary
- Individual breakdown for each person showing:
  - Individual items
  - Shared items portion
  - Tax + tip share
  - **Total amount owed**

## Example Session
```
============================================================
  🍽️  BILL SPLITTER
============================================================

STEP 1: Bill Amount
------------------------------------------------------------
Enter bill amount (before tax): $100.00

------------------------------------------------------------
STEP 2: Tax
------------------------------------------------------------
Enter tax amount: $8.50

------------------------------------------------------------
STEP 3: Tip
------------------------------------------------------------
Is tip already included in the bill? (yes/no): no
Would you like to add a tip? (yes/no): yes
Enter tip percentage (e.g., 18 for 18%): 20
Tip amount: $20.00

------------------------------------------------------------
STEP 4: Number of People
------------------------------------------------------------
How many people are splitting? 2

------------------------------------------------------------
STEP 5: Individual Orders
For each person, enter items as comma-separated amounts.
Example: 12.50, 8.99, 15.00
Leave blank if no individual items.

Person 1:
  Name: Alice
  Items (comma-separated amounts): 45.00, 15.00
  Subtotal: $60.00

Person 2:
  Name: Bob
  Items (comma-separated amounts): 40.00
  Subtotal: $40.00

------------------------------------------------------------
STEP 6: Shared Items
------------------------------------------------------------

Were any items shared between people? (yes/no): no

============================================================
FINAL RESULTS
============================================================

Bill Summary:
  Bill (before tax): $100.00
  Tax:               $8.50
  Tip:               $20.00
  Total:             $128.50
  Split among:       2 people

Individual Breakdowns:
------------------------------------------------------------

Alice:
  Individual items: $45.00, $15.00
  Individual subtotal: $60.00
  Shared items total:  $0.00
  Tax + Tip share:     $14.25
  ------------------------------
  TOTAL OWED:          $74.25

Bob:
  Individual items: $40.00
  Individual subtotal: $40.00
  Shared items total:  $0.00
  Tax + Tip share:     $14.25
  ------------------------------
  TOTAL OWED:          $54.25

============================================================
Thank you! 😊
```

## Business Logic

### Money Calculations
- All calculations use `Decimal` with 28-digit precision
- Display values rounded to 2 decimal places
- Tax and tip are split evenly among all people
- Shared items are divided exactly by the number of sharers

### Formula
```
Tax + Tip per person = (tax + tip) / number_of_people
Total Owed = individual_subtotal + shared_items_total + tax_tip_share
```

## Input Validation

The application validates all inputs:
- ✅ Monetary amounts must be valid numbers (≥ 0)
- ✅ Names must be non-empty and unique
- ✅ Number of people must be at least 2
- ✅ Shared items must have at least 2 sharers
- ✅ All names in shared items must match entered people
- ✅ Re-prompts on invalid input with helpful error messages

## Code Structure

The codebase follows clean architecture principles:
```
bill_splitter.py
├── Input Functions
│   ├── read_money()          - Read monetary amounts
│   ├── read_percentage()     - Read tip percentage
│   ├── read_yes_no()         - Yes/no questions
│   ├── read_int_min()        - Integer with minimum value
│   └── parse_items_csv()     - Parse comma-separated items
├── Calculation Functions
│   ├── compute_tip_from_percent()
│   └── compute_tax_tip_share()
├── Data Collection
│   ├── collect_people()      - Gather person data
│   └── handle_shared_items() - Process shared items
├── Display Functions
│   ├── render_bill_summary()
│   └── render_person_breakdown()
└── Main Orchestration
    ├── main()                - Primary flow
    └── run_demo_calculations() - Testing mode
```

### Design Principles
- **No nested functions** - All functions are top-level
- **Type hints** - Full type annotation for clarity
- **Single responsibility** - Each function has one clear purpose
- **No external dependencies** - Uses only Python standard library

## Testing

Run the built-in demo to verify calculations:
```bash
python3 bill_splitter.py --demo
```

This tests:
- Tip calculation with >20% (generosity message)
- Included tip path
- No tip scenario
- Shared item splitting
- Tax + tip per-person calculation
- Decimal precision handling


## Tips for Best Results

1. **Have the receipt ready** - Makes entering items easier
2. **Round to cents** - Enter amounts as they appear (e.g., $12.99)
3. **Use descriptive names** - Makes the breakdown easier to read
4. **Double-check shared items** - Ensure all sharers are listed
5. **Verify totals** - Check that individual items roughly match the bill

## Troubleshooting

**Q: "Name already used" error?**  
A: Each person must have a unique name. Use nicknames if needed (Alice, Alice2).

**Q: Shared item names don't match?**  
A: Names are case-insensitive but must match exactly. Check for typos.

**Q: Getting decimal precision errors?**  
A: The app uses `Decimal` to prevent this. If you see issues, please report a bug.

**Q: Can I split unevenly?**  
A: Tax and tip are always split evenly. Use individual/shared items for uneven splits.

## Contributing

Contributions are welcome! Please ensure:
- Code follows the existing style
- All functions have type hints
- No external dependencies are added
- Tests pass (run `--demo` mode)


## Author

Built with ❤️ for fair bill splitting

---

**Questions or Issues?**  
Open an issue on GitHub or contact the maintainer.

**Found it useful?**  
⭐ Star the repository and share with friends!

