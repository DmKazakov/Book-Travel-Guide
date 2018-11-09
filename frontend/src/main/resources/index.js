function sendPostRequest(id, action) {
    var xhr = new XMLHttpRequest();
    var body = 'id=' + encodeURIComponent(id) + '&action=' + encodeURIComponent(action);

    xhr.open("POST", '/quote_review', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
}

var list = document.querySelector('ul');
list.addEventListener('click', function (ev) {
    if (ev.target.tagName === 'LI' && ev.target.classList !== 'interesting') {
        ev.target.classList = 'interesting';
        sendPostRequest(ev.target.id, "inc"); //TODO check
    }
}, false);

function fetchLocationSet() {
    function addLocation(locationContext) {
        var location = locationContext['location'];
        var quote = locationContext['quote'];
        var id = locationContext['id'];
        var listItem = document.createElement('li');
        listItem.id = id;
        listItem.innerHTML = "<b>Location:<\b> " + location + "<br>" + quote;
        list.appendChild(listItem);
    }

    var url = "/locations_set"; //TODO
    fetch(url).then(function (response) {
        return response.json();
    }).then(function (locations) {
        var locationsArray = locations['locations'];
        for (var i = 0; i < locationsArray.length; i++) {
            addLocation(locationsArray[i]);
        }
    });
}

function nextSet() {
    var listItem = document.querySelectorAll('.locations li');
    for (var i = 0; i < listItem.length; i++) {
        if (listItem[i].className !== 'interesting') {
            sendPostRequest(listItem[i].id, "dec")
        }
        list.removeChild(listItem[i]);
    }
    fetchLocationSet();
}

var updateButton = document.getElementById('update');
updateButton.addEventListener('click', nextSet);
nextSet();