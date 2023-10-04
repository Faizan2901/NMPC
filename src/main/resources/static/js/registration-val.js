function validateForm() {
    // Reset previous error messages
    $(".error").text("");

    // Get form values
    const username = $("#username").val();
    const password = $("#password").val();
    const firstName = $("#firstName").val();
    const lastName = $("#lastName").val();
    const email = $("#email").val();

    // Regular expressions for validation
    const usernameRegex = /[a-zA-Z0-1_]{5,}/; // Only letters, numbers, and underscores allowed
    const passwordRegex = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{5,}$/; // Minimum 8 characters, at least one letter and one number
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // Valid email address pattern
    const firstnameRegex=/[a-zA-Z\s]+/;
    const lastnameRegex=/[a-zA-Z\s]+/;

    // Validate username
    if (!username) {
        $("#username-error").text("Username is required.");
        return;
    } else if (!usernameRegex.test(username)) {
        $("#username-error").text("Username can only contain letters, numbers, and underscores and at least 5 character long.");
        return;
    }

    // Validate password
    if (!password) {
        $("#password-error").text("Password is required.");
        return;
    } else if (!passwordRegex.test(password)) {
        $("#password-error").text("Password must be at least 5 character long and at least one uppercase and lowercase and Special character");
        return;
    }

    // Validate first name
    if (!firstName) {
        $("#firstName-error").text("First Name is required.");
        return;
    }else if (!firstnameRegex.test(firstName)){
        $("#firstName-error").text("First name should be in alphabetic format");
        return;
    }

    // Validate last name
    if (!lastName) {
        $("#lastName-error").text("Last Name is required.");
        return;
    }else if (!lastnameRegex.test(lastName)){
             $("#lastName-error").text("Last name should be in alphabetic format");
             return;
         }

    // Validate email
    if (!email) {
        $("#email-error").text("Email is required.");
        return;
    } else if (!emailRegex.test(email)) {
        $("#email-error").text("Invalid email address.");
        return;
    }

    // If all fields are filled and meet validation criteria, submit the form
    $("#registration-form").submit();
}
