/**
 * Created by franc on 9/21/2016.
 */
var stompClient = null;
$(document).ready(function () {
    getPreviousMessages();
    connect();
});

$('#sendMessage').click(function () {
    sendMessage();
});
function connect() {
    var socket = new SockJS("/chat");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('frame: ' + frame);
        stompClient.subscribe('/notifications/new_message', function (reply) {
            console.log('message: ' + JSON.parse(reply.body).text);
        });
    });
}

function sendMessage(e) {
    stompClient.send("/socks/new_message", {}, JSON.stringify({text: $('#textarea').val()}));
}

function displayMessage(message) {
    if (message.isOwner) {

    } else {

    }
}

function getPreviousMessages() {
    $.ajax({url: "/chat/messages"}).then(function (data) {
        return data;
    });
}