package com.littlegig.app.services

import android.util.Patterns
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InputValidationService @Inject constructor() {

    companion object {
        private val PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
        )
        private val PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$")
        private val KENYA_PHONE_PATTERN = Pattern.compile("^(?:254|\\+254|0)?([17]\\d{8})$")
        private val USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$")
        private val EVENT_TITLE_PATTERN = Pattern.compile("^[\\w\\s\\-&.,!?()]{3,100}$")
        private val EVENT_DESCRIPTION_PATTERN = Pattern.compile("^[\\w\\s\\-&.,!?()\\n]{10,1000}$")
        private val PRICE_PATTERN = Pattern.compile("^\\d+(\\.\\d{1,2})?$")
    }

    // User registration and profile validation
    fun validateUserRegistration(
        email: String,
        password: String,
        confirmPassword: String,
        firstName: String,
        lastName: String,
        phone: String
    ): ValidationResult {
        val errors = mutableListOf<ValidationError>()
        
        // Email validation
        if (!isValidEmail(email)) {
            errors.add(ValidationError("email", "Please enter a valid email address"))
        }
        
        // Password validation
        if (!isValidPassword(password)) {
            errors.add(ValidationError("password", "Password must be at least 8 characters with uppercase, lowercase, number, and special character"))
        }
        
        // Password confirmation
        if (password != confirmPassword) {
            errors.add(ValidationError("confirmPassword", "Passwords do not match"))
        }
        
        // Name validation
        if (!isValidName(firstName)) {
            errors.add(ValidationError("firstName", "First name must be 2-50 characters"))
        }
        
        if (!isValidName(lastName)) {
            errors.add(ValidationError("lastName", "Last name must be 2-50 characters"))
        }
        
        // Phone validation
        if (!isValidPhone(phone)) {
            errors.add(ValidationError("phone", "Please enter a valid phone number"))
        }
        
        return ValidationResult(errors.isEmpty(), errors)
    }

    // Event creation and editing validation
    fun validateEventCreation(
        title: String,
        description: String,
        dateTime: Long,
        location: String,
        price: String?,
        category: String,
        capacity: Int?
    ): ValidationResult {
        val errors = mutableListOf<ValidationError>()
        
        // Title validation
        if (!isValidEventTitle(title)) {
            errors.add(ValidationError("title", "Event title must be 3-100 characters"))
        }
        
        // Description validation
        if (!isValidEventDescription(description)) {
            errors.add(ValidationError("description", "Event description must be 10-1000 characters"))
        }
        
        // Date validation
        if (!isValidEventDate(dateTime)) {
            errors.add(ValidationError("dateTime", "Event date must be in the future"))
        }
        
        // Location validation
        if (!isValidLocation(location)) {
            errors.add(ValidationError("location", "Please enter a valid location"))
        }
        
        // Price validation
        if (price != null && price.isNotEmpty() && !isValidPrice(price)) {
            errors.add(ValidationError("price", "Please enter a valid price"))
        }
        
        // Category validation
        if (!isValidEventCategory(category)) {
            errors.add(ValidationError("category", "Please select a valid event category"))
        }
        
        // Capacity validation
        if (capacity != null && !isValidCapacity(capacity)) {
            errors.add(ValidationError("capacity", "Capacity must be between 1 and 10000"))
        }
        
        return ValidationResult(errors.isEmpty(), errors)
    }

    // Payment validation
    fun validatePayment(
        amount: Double,
        currency: String,
        paymentMethod: String,
        cardNumber: String? = null,
        expiryDate: String? = null,
        cvv: String? = null
    ): ValidationResult {
        val errors = mutableListOf<ValidationError>()
        
        // Amount validation
        if (!isValidAmount(amount)) {
            errors.add(ValidationError("amount", "Amount must be greater than 0"))
        }
        
        // Currency validation
        if (!isValidCurrency(currency)) {
            errors.add(ValidationError("currency", "Please select a valid currency"))
        }
        
        // Payment method validation
        if (!isValidPaymentMethod(paymentMethod)) {
            errors.add(ValidationError("paymentMethod", "Please select a valid payment method"))
        }
        
        // Card validation for card payments
        if (paymentMethod == "card") {
            if (cardNumber == null || !isValidCardNumber(cardNumber)) {
                errors.add(ValidationError("cardNumber", "Please enter a valid card number"))
            }
            
            if (expiryDate == null || !isValidExpiryDate(expiryDate)) {
                errors.add(ValidationError("expiryDate", "Please enter a valid expiry date"))
            }
            
            if (cvv == null || !isValidCVV(cvv)) {
                errors.add(ValidationError("cvv", "Please enter a valid CVV"))
            }
        }
        
        return ValidationResult(errors.isEmpty(), errors)
    }

    // Chat message validation
    fun validateChatMessage(
        message: String,
        messageType: String,
        recipientId: String
    ): ValidationResult {
        val errors = mutableListOf<ValidationError>()
        
        // Message content validation
        if (!isValidMessageContent(message, messageType)) {
            errors.add(ValidationError("message", "Message content is invalid"))
        }
        
        // Message type validation
        if (!isValidMessageType(messageType)) {
            errors.add(ValidationError("messageType", "Invalid message type"))
        }
        
        // Recipient validation
        if (!isValidUserId(recipientId)) {
            errors.add(ValidationError("recipientId", "Invalid recipient"))
        }
        
        return ValidationResult(errors.isEmpty(), errors)
    }

    // Search query validation
    fun validateSearchQuery(
        query: String,
        categories: List<String>,
        dateRange: Pair<Long?, Long?>?,
        priceRange: Pair<Double?, Double?>?
    ): ValidationResult {
        val errors = mutableListOf<ValidationError>()
        
        // Query text validation
        if (query.isNotEmpty() && !isValidSearchQuery(query)) {
            errors.add(ValidationError("query", "Search query contains invalid characters"))
        }
        
        // Categories validation
        if (categories.isNotEmpty() && !categories.all { isValidEventCategory(it) }) {
            errors.add(ValidationError("categories", "One or more categories are invalid"))
        }
        
        // Date range validation
        if (dateRange != null) {
            val (start, end) = dateRange
            if (start != null && end != null && start > end) {
                errors.add(ValidationError("dateRange", "Start date must be before end date"))
            }
        }
        
        // Price range validation
        if (priceRange != null) {
            val (min, max) = priceRange
            if (min != null && max != null && min > max) {
                errors.add(ValidationError("priceRange", "Minimum price must be less than maximum price"))
            }
            if (min != null && min < 0) {
                errors.add(ValidationError("priceRange", "Price cannot be negative"))
            }
        }
        
        return ValidationResult(errors.isEmpty(), errors)
    }

    // Individual validation methods
    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8 && PASSWORD_PATTERN.matcher(password).matches()
    }

    private fun isValidName(name: String): Boolean {
        return name.length in 2..50 && name.matches(Regex("^[a-zA-Z\\s'-]+$"))
    }

    private fun isValidPhone(phone: String): Boolean {
        return phone.isNotEmpty() && (PHONE_PATTERN.matcher(phone).matches() || 
                KENYA_PHONE_PATTERN.matcher(phone).matches())
    }

    private fun isValidEventTitle(title: String): Boolean {
        return title.length in 3..100 && EVENT_TITLE_PATTERN.matcher(title).matches()
    }

    private fun isValidEventDescription(description: String): Boolean {
        return description.length in 10..1000 && EVENT_DESCRIPTION_PATTERN.matcher(description).matches()
    }

    private fun isValidEventDate(dateTime: Long): Boolean {
        return dateTime > System.currentTimeMillis()
    }

    private fun isValidLocation(location: String): Boolean {
        return location.length in 3..200
    }

    private fun isValidPrice(price: String): Boolean {
        return PRICE_PATTERN.matcher(price).matches() && price.toDoubleOrNull() != null
    }

    private fun isValidEventCategory(category: String): Boolean {
        return category.isNotEmpty() && category.length <= 50
    }

    private fun isValidCapacity(capacity: Int): Boolean {
        return capacity in 1..10000
    }

    private fun isValidAmount(amount: Double): Boolean {
        return amount > 0
    }

    private fun isValidCurrency(currency: String): Boolean {
        return currency in listOf("KES", "USD", "EUR", "GBP")
    }

    private fun isValidPaymentMethod(method: String): Boolean {
        return method in listOf("card", "mpesa", "airtel", "paypal")
    }

    private fun isValidCardNumber(cardNumber: String): Boolean {
        return cardNumber.replace(" ", "").length in 13..19 && 
               cardNumber.replace(" ", "").all { it.isDigit() }
    }

    private fun isValidExpiryDate(expiryDate: String): Boolean {
        return try {
            val parts = expiryDate.split("/")
            if (parts.size != 2) return false
            
            val month = parts[0].toInt()
            val year = parts[1].toInt()
            
            month in 1..12 && year >= 23
        } catch (e: Exception) {
            false
        }
    }

    private fun isValidCVV(cvv: String): Boolean {
        return cvv.length in 3..4 && cvv.all { it.isDigit() }
    }

    private fun isValidMessageContent(message: String, type: String): Boolean {
        return when (type) {
            "text" -> message.length in 1..1000
            "image" -> message.isNotEmpty() // URL or file path
            "video" -> message.isNotEmpty() // URL or file path
            else -> false
        }
    }

    private fun isValidMessageType(type: String): Boolean {
        return type in listOf("text", "image", "video", "location", "file")
    }

    private fun isValidUserId(userId: String): Boolean {
        return userId.isNotEmpty() && userId.length <= 50
    }

    private fun isValidSearchQuery(query: String): Boolean {
        return query.length <= 200 && !query.contains(Regex("[<>\"'&]"))
    }

    // Sanitization methods
    fun sanitizeInput(input: String): String {
        return input.trim()
            .replace(Regex("[<>\"'&]"), "")
            .replace(Regex("\\s+"), " ")
    }

    fun sanitizeHtmlInput(input: String): String {
        return input.trim()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
            .replace("&", "&amp;")
    }

    // Rate limiting validation
    fun validateRateLimit(
        userId: String,
        action: String,
        currentCount: Int,
        maxCount: Int,
        timeWindow: Long
    ): ValidationResult {
        val errors = mutableListOf<ValidationError>()
        
        if (currentCount >= maxCount) {
            errors.add(ValidationError("rateLimit", "Rate limit exceeded. Please try again later."))
        }
        
        return ValidationResult(errors.isEmpty(), errors)
    }

    // Data classes
    data class ValidationResult(
        val isValid: Boolean,
        val errors: List<ValidationError>
    )

    data class ValidationError(
        val field: String,
        val message: String
    )
}
