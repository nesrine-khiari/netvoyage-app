const SockJS = require('sockjs-client'); // Import SockJS client
const Stomp = require('stompjs'); // Import STOMP client

const socket = new SockJS('http://localhost:8088/ws'); // Adjust the URL if needed
const stompClient = Stomp.over(socket);

stompClient.connect({}, () => {
    console.log('Connected to WebSocket');

    // Subscribe to discussion ID 1
    stompClient.subscribe('/topic/discussion/1', (message) => {
        console.log('Received message:', JSON.parse(message.body));
    });

    // Send a test message to discussion ID 1
    const message = {
        content: 'Hello, this is a test message!',
        voyageur: {  numVoyageur: 1,
            email: "ali.bensalah@example.com",
            firstname: "Ali",
            lastname: "Ben Salah",
            phone: "+21698765432" } // Adjust voyageur data as needed
    };

    stompClient.send('/app/chat/1', {}, JSON.stringify(message));
    console.log('Message sent to discussion 1');
}, (error) => {
    console.error('WebSocket connection error:', error);
});