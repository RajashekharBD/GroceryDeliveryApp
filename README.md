# Grocery Delivery App (Blinkit-Style)

A Blinkit-inspired grocery delivery Android app built with Kotlin, XML layouts, MVVM architecture, and Room database.

## Tech Stack

| Component          | Version         |
|--------------------|-----------------|
| Kotlin             | 2.2.10          |
| AGP                | 9.2.1           |
| Gradle             | 9.4.1           |
| compileSdk/targetSdk | 36           |
| minSdk             | 24              |
| Navigation         | 2.8.2           |
| Room               | 2.6.1           |
| Glide              | 4.16.0          |
| Material           | 1.12.0          |

## Features

### Authentication
- **Login** — 10-digit mobile number, any number accepted
- **OTP** — Hardcoded OTP `1234`

### Home
- Search icon in toolbar (expands when tapped, real-time product filtering)
- Horizontal category list (Fruits, Dairy, Snacks, Beauty)
- Product grid with image, name, price, discount, rating, stock status
- Tap product → saves to Room cart with full product info

### Cart (Room DB Persistent)
- Full product image, name, price displayed
- Quantity +/- buttons
- Swipe left/right to delete
- Cart summary: total items, total amount, 15% savings, delivery fee (free above ₹500)

### Checkout
- Delivery address input
- Payment mode: Cash on Delivery / Online Payment
- Order summary with actual cart amounts

### Success
- Random order ID
- Expected delivery time
- Continue Shopping → Home
- Track Order → toast

## Navigation Flow

```
Login → OTP → Home → Cart → Checkout → Success → Home
```

Each screen has a back arrow in the orange toolbar.

## How to Build & Run

```bash
# Set JAVA_HOME to Android Studio's bundled JDK
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"

# Build and install on connected emulator/device
./gradlew :app:uninstallDebug :app:installDebug
```

Or open in Android Studio and press **Run** (Shift+F10).

## Testing

1. Enter any 10-digit number → **Verify**
2. Enter OTP `1234` → **Verify OTP**
3. Browse products, tap to add to cart
4. Tap orange cart FAB (bottom-right)
5. Adjust quantities, swipe to remove
6. **PROCEED TO CHECKOUT**
7. Enter address, select payment → **PLACE ORDER**
8. **CONTINUE SHOPPING** or **TRACK ORDER**

## Project Structure

```
app/src/main/java/com/example/grocery/
├── data/          # CartDao, CartDatabase (Room)
├── model/         # CartItem, Product, Category
└── ui/
    ├── auth/      # LoginFragment, OtpFragment, AuthViewModel
    ├── home/      # HomeFragment, GroceryViewModel, adapters
    ├── cart/      # CartFragment, CartViewModel, CartAdapter
    ├── checkout/  # CheckoutFragment
    └── success/   # SuccessFragment
```
## 📱 Download APK

Download the latest version here:
https://github.com/RajashekharBD/GroceryDeliveryApp/releases/tag/v1.0