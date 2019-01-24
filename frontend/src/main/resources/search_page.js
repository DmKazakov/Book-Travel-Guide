$(document).ready(function () {
    const urlParams = new URLSearchParams(window.location.search);
    const location = urlParams.get('search');
    load(`/search?location=${location}`);
});