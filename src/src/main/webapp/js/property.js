function toggleFields() {
    const propertyType = document.getElementById("propertyType").value;

    const houseFields = document.getElementById("houseFields");
    const apartmentFields = document.getElementById("apartmentFields");
    const landFields = document.getElementById("landFields");

    houseFields.style.display = "none";
    apartmentFields.style.display = "none";
    landFields.style.display = "none";

    if (propertyType === "House") {
        houseFields.style.display = "block";
    } else if (propertyType === "Apartment") {
        apartmentFields.style.display = "block";
    } else if (propertyType === "Land") {
        landFields.style.display = "block";
    }
}

window.onload = toggleFields;