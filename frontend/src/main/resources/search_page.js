$(document).ready(function () {
    function addLocation(locationContext) {
        const title = locationContext['title'];
        const quote = locationContext['sentence'];
        const listItem = `<li>
            <b>Title: </b> ${title} <br>
            ${quote} 
            </li>`;
        $(".main").append(listItem);
    }

    const urlParams = new URLSearchParams(window.location.search);
    const location = urlParams.get('search');

    $.get("/search", {
        location: location
    }, function (response) {
        const locations = JSON.parse(response);
        const locationsArray = locations['locations'];
        for (let i = 0; i < locationsArray.length; i++) {
            addLocation(locationsArray[i]);
        }
    });
});