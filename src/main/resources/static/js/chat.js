/**
 * Created by franc on 9/21/2016.
 */
var currentUser = null;
var stompClient = null;
var templateOwner = null;
var templateNotOwner = null;
var templateMessage = null;
$(document).ready(function () {
    getCurrentUser();
    loadTemplates();
    showPreviousMessages();
    connect();
});

$(function () {
    $('[data-toggle="tooltip"]').tooltip()
});

$("#textarea").keydown(function (event) {
    if (event.keyCode == 13 && !event.shiftKey) {
        event.preventDefault();
        $("#msg-input-form").submit();
    }
});

$("#msg-input-form").submit(function (event) {
    var textarea = $("#textarea");
    event.preventDefault();
    sendMessage(textarea.val());
    textarea.val("");
});

function loadTemplates() {
    $.when(getTemplate("/js/templates/msg-group-owner.html")).then(function (response) {
        templateOwner = $.templates(response);
    });
    $.when(getTemplate("/js/templates/msg-group.html")).then(function (response) {
        templateNotOwner = $.templates(response);
    });
    $.when(getTemplate("/js/templates/msg-text.html")).then(function (response) {
        templateMessage = $.templates(response);
    });
}

function getTemplate(path) {
    return $.ajax({url: path});
}

function showPreviousMessages() {
    $.when(getPreviousMessages()).then(function (data) {
        $.each(data, function () {
            displayMessage(this);
        })
    })
}

function getPreviousMessages() {
    return $.ajax({url: "/chat/messages"});
}

function connect() {
    var socket = new SockJS("/chat");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/notifications/new_message', function (reply) {
            displayMessage(JSON.parse(reply.body));
        });
    });
}

function sendMessage(text) {
    stompClient.send("/socks/new_message", {}, JSON.stringify({text: text}));
}

function displayMessage(message) {
    var container = $('#msgr-container');
    var msgGroup = lastMsgGroup(message.user.id);
    if (msgGroup == null) {
        if (currentUser.id == message.user.id) {
            msgGroup = $.parseHTML(templateOwner.render(message));
        } else {
            msgGroup = $.parseHTML(templateNotOwner.render(message));
        }
        console.log(msgGroup);
        container.append(msgGroup);
    }
    $("[data-msg-group]", msgGroup).append($.parseHTML(templateMessage.render(message)));
    container[0].scrollTop = container[0].scrollHeight;
}

function lastMsgGroup(userId) {
    var lastGroup = $("#msgr-container > div:last");
    if (lastGroup.attr("data-user-id") == userId) {
        return $("#msgr-container > div:last");
    }
    return null;
}

function getCurrentUser() {
    $.ajax({url: "/user/current"}).then(function (data) {
        currentUser = data;
    })
}