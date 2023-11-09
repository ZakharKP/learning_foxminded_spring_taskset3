function sortEntitiesById(className) {
    console.log("Sorting function called with class name:", className);

    const ulElements = document.getElementsByClassName(className);

    console.log("UL Elements:", ulElements);

    // Iterate over each UL element with the specified class
    for (const ulElement of ulElements) {
        const entitiesArray = Array.from(ulElement.getElementsByTagName('li'));

        console.log("Entities Array:", entitiesArray);

        // Sort the entitiesArray based on the specified property
        entitiesArray.sort((a, b) => {
            const idA = parseInt(a.getAttribute("id"), 10);
            const idB = parseInt(b.getAttribute("id"), 10);

            console.log("Sorting elements with id's:", idA, idB);

            return idA - idB;
        });

        // Clear the UL element
        ulElement.innerHTML = '';

        // Render the sorted entities
        entitiesArray.forEach(entity => ulElement.appendChild(entity));
    }
}
