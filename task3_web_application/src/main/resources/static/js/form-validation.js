// form-validation.js

function updateSubmitButtonStatus(formId) {
    const form = document.getElementById(formId);
    const checkboxes = form.querySelectorAll('input[type="checkbox"]');
    const submitButton = form.querySelector('button[type="submit"]');

    // Function to check if any checkboxes are selected
    function isAnyChecked() {
        return Array.from(checkboxes).some(checkbox => checkbox.checked);
    }

    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            const anyChecked = isAnyChecked();
            submitButton.disabled = !anyChecked;
            console.log(`Submit button disabled: ${!anyChecked}`);
        });
    });

    // Intercept form submission
    form.addEventListener('submit', event => {
        if (!isAnyChecked()) {
            event.preventDefault(); // Prevent form submission
            console.log('Form submission prevented: No checkboxes selected.');
            alert('Please select at least one item.'); // Show an alert or customize the behavior
        } else {
            console.log('Form submitted.');
        }
    });

    console.log('Form validation script initialized.');
}
