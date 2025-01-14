# Currency Edittext
This project is a fork of the original [currency-edittext](https://github.com/AbhinayMe/currency-edittext) repository by [Abhinay Me](https://github.com/AbhinayMe) and updated by  [Joel Armah](https://github.com/joelarmah/currency-edittext).

A Custom EditText implementation that allows formatting of currency-based numeric inputs.

This README file has been updated to note differences/tweaks found when attempting to use this in Android Studio Ladybug (as found in the Help --> About dialog):
- Android Studio Ladybug | 2024.2.1 Patch 3
- Runtime version: 21.0.3+-12282718-b509.11 amd64


## Installation

Updated for Android Studio Ladybug:

Add the maven & jitpack.io line as shown below, into your app's settings.gradle.kts file if you experience "Failure to resolve ..." errors, related to using this component:
```groovy
...
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
...
```

Original instructions:

Add this in your app's build.gradle file:
```groovy
dependencies {
  implementation 'com.github.joelarmah:currency-edittext:1.0.0'
}
```

Updated for Android Studio Ladybug:

Perform a sync of this file, after adding the maven & jitpack.io detail shown above. 
The failure to resolve error should no longer be an issue after the sync has been performed.

## Implementation

XML

```
<com.github.joelarmah.currency.CurrencyEditText
        android:id="@+id/currencyEditText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Type value"
        android:inputType="number"
        android:textSize="24sp" />
```

Code

```
 override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

       viewBinding.currencyEditText.apply {
            setCurrency(CurrencySymbols.GHANA) or Use any symbol "$"
            setDelimiter(false)
            setSpacing(false)
            setDecimals(true)
            setSeparator(".")
        }
}
```

## Customizing

The following attributes can be manipulated:

- Currency by specifying the country
- Spacing between currency and value
- Delimeter
- Decimals
- Thousands Separator Symbol

### Currency

Specify the currency by setting the country of your choice.

```
currencyEditText.Currency = Currency.GHANA;
```

Currency can also be disabled by:

```
currencyEditText.Currency = Currency.NONE;
```

#### Custom Currency/Symbol

If a custom symbol that is not included in the library is required, any string value can be used since the the `Currency` attribute expects a `String` value.

```
currencyEditText.Currency = "TEST";
```

Which produces:
>TEST 450.00

**Note:** Currency is set to your app's Local currency by default.

### Spacing

The spacing between the currency and the value can be specified by:

```
currencyEditText.Spacing = true;
```

**Note:** Spacing is `false` by default.

### Delimeter

The delimeter attribute allows the addition of a `.` symbol after displaying the currency.

> Rs.100

> Rp.100

**Note:** Delimeter is `false` by default.

### Decimals

Decimals can be turned off for the EditText using:

```
currencyEditText.Decimals = false;
```

This outputs the following:

> $100,000

### Separator

The Thousands Separator can be customized as required with any custom symbol to suit the currency formats of different countries. Example: Indonesia -> 12.000.000 (Using . instead of , as the separator)

**NOTE: Decimals must be set as `false` in order avoid conflicts in getting a clean Double or Integer output**

### Getting Clean Output

A `Double` value without Commas, Currency and Decimal places can be retrieved using:

`double cleanOutput = currencyEditText.getCleanDoubleValue();`

An `Integer` value without Commas, Currency and Decimal places can be retrieved using:

`int cleanOutput = currencyEditText.getCleanIntValue();`
