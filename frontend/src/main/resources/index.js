var list = document.querySelector('ul');

list.addEventListener('click', function (ev) {
    if (ev.target.tagName === 'LI' && ev.target.classList !== 'interesting') {
        ev.target.classList = 'interesting';
        //TODO send inc query
    }
}, false);

function fetchLocationSet() {
    function addLocation(locationContext) {
        var location = locationContext['location'];
        var qoute = locationContext['quote'];
        var id = locationContext['id'];
        var listItem = document.createElement('li');
        listItem.id = id;
        listItem.innerHTML = "<b>Location:<\b> " + location + "<br>" + qoute;
        list.appendChild(listItem)
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
        if(listItem[i].className !== 'interesting') {
            //TODO send deq query
        }
        list.removeChild(listItem[i])
    }
    fetchLocationSet()
}