$(document).ready(function () {
    function sendPostRequest(id, action) {
        $.post("/quote_review",
            {
                id: id,
                action: action
            }
        )
    }

    function fetchLocationSet() {
        function addLocation(locationContext) {
            const location = locationContext['location'];
            const quote = locationContext['sentence'];
            const id = locationContext['id'];
            const listItem = `<li id=${id}> <b>Location: </b> ${location} <br> ${quote} </li>`;
            $("ul").append(listItem);
        }

        $.get("/unreviewed_locations", {}, function (response) {
            const locations = JSON.parse(response);
            const locationsArray = locations['locations'];
            for (let i = 0; i < locationsArray.length; i++) {
                addLocation(locationsArray[i]);
            }

            $("li").click(function () {
                if (this.className !== 'interesting') {
                    this.className = 'interesting';
                    sendPostRequest(this.id, "inc");
                }
            });
        });
    }

    $("button").click(function nextSet() {
        $("li").each(function () {
            if (this.className !== 'interesting') {
                sendPostRequest(this.id, "dec");
            }
            $(`#${this.id}`).remove();
        });
        fetchLocationSet();
    });

    fetchLocationSet()
});