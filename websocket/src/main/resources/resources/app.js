var stompClient = null;

function connect() {
    var socket = new SockJS('/xxx');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
    });
}
