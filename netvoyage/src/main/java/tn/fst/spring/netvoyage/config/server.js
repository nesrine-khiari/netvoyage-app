const SockJS = require('sockjs-client'); // Import SockJS client
const Stomp = require('stompjs'); // Import STOMP client

const socket = new SockJS('http://localhost:8088/ws'); // Adjust the URL if needed
const stompClient = Stomp.over(socket);

// Define users
const voyageur1 = {
    numVoyageur: 1,
    email: "ali.bensalah@example.com",
    firstname: "Ali",
    lastname: "Ben Salah",
    phone: "+21698765432"
};

const voyageur2 = {
    numVoyageur: 2,
    email: "leila.mansour@example.com",
    firstname: "Leila",
    lastname: "Mansour",
    phone: "+21612345678"
};

// Select user dynamically (Run with 'node chatClient.js 1' or 'node chatClient.js 2')
const userId = process.argv[2] === '1' ? 1 : 2;
const user = userId === 1 ? voyageur1 : voyageur2;
const recipient = userId === 1 ? voyageur2 : voyageur1; // Other user

stompClient.connect({}, () => {
    console.log(`✅ User ${user.firstname} (${user.numVoyageur}) connected to WebSocket`);

    // Subscribe to discussion ID 1

        stompClient.subscribe('/topic/discussion/1', (message) => {
            console.log(`this is the message: ${message} `);

            const receivedMessage = JSON.parse(message.body);

            console.log(`📩 ${user.firstname} received: "${receivedMessage.content}" from ${receivedMessage.senderName}`);
        });



    // Simulate sending a message after subscription
    setTimeout(() => {
        const message = {
            content: `Hello ${recipient.firstname}, this is ${user.firstname}!`,
            senderId: user.numVoyageur // Correct field name
        };


        stompClient.send('/app/chat/1', {}, JSON.stringify(message));
        console.log(`📤 ${user.firstname} sent a message: "${message.content}"`);
    }, 2000); // Wait 2 seconds to ensure subscription is active

}, (error) => {
    console.error('❌ WebSocket connection error:', error);
});
