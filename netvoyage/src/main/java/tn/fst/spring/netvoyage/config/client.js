const SockJS = require('sockjs-client');
const Stomp = require('stompjs');
const readline = require('readline');

const socket = new SockJS('http://localhost:8088/ws');
const stompClient = Stomp.over(socket);

const voyageur1 = { numVoyageur: 1, email: "ali.bensalah@example.com", firstname: "Ali", lastname: "Ben Salah", phone: "+21698765432" };
const voyageur2 = { numVoyageur: 2, email: "leila.mansour@example.com", firstname: "Leila", lastname: "Mansour", phone: "+21612345678" };
const voyageur3 = { numVoyageur: 3, email: "aliaaa.benmoula@example.com", firstname: "Alia", lastname: "Ben Moula", phone: "+21690765432" };

const userId = process.argv[2] === '1' ? 1 : (process.argv[2] === '2' ? 2 : 3);
const user = userId === 1 ? voyageur1 : (userId === 2 ? voyageur2 : voyageur3);
let discussionId;

// Set the discussionId dynamically based on the user
if (userId === 1) {
    discussionId = 1; // Discussion 1: Voyageur 1 and Voyageur 2
} else if (userId === 2) {
    discussionId = 1; // Discussion 1: Voyageur 1 and Voyageur 2
} else {
    discussionId = 1; // Discussion 2: Voyageur 2 and Voyageur 3
}

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

let lastReceivedMessage = null; // Store the last received message

stompClient.connect({}, () => {
    console.log(`✅ ${user.firstname} (${user.numVoyageur}) connecté au WebSocket`);

    // Subscribe to the appropriate discussion based on the discussionId
    stompClient.subscribe(`/topic/discussion/${discussionId}`, (message) => {
        const receivedMessage = JSON.parse(message.body);
        console.log(`📩 ${user.firstname} a reçu : "${receivedMessage.content}" de ${receivedMessage.senderName}`);
        lastReceivedMessage = receivedMessage; // Store the last message for marking as read
    });

    const sendMessage = () => {
        rl.question("✍️ Tapez votre message ('read' pour marquer le dernier message comme lu, 'exit' pour quitter) : ", (input) => {
            if (input.toLowerCase() === 'exit') {
                console.log("👋 Déconnexion...");
                stompClient.disconnect();
                rl.close();
                process.exit(0);
            }

            if (input.toLowerCase() === 'read') {
                if (lastReceivedMessage) {
                    console.log(`numMessage: ${lastReceivedMessage.numMessage}`);
                    stompClient.send('/app/message/read', {}, JSON.stringify({
                        numMessage: lastReceivedMessage.numMessage,
                        senderId: user.numVoyageur
                    }));
                    console.log("✅ Message marqué comme lu.");
                } else {
                    console.log("❌ Aucun message à marquer comme lu.");
                }
            } else {
                const message = { content: input, senderId: user.numVoyageur };
                stompClient.send(`/app/discussion/${discussionId}`, {}, JSON.stringify(message));
                console.log(`📤 ${user.firstname} a envoyé : "${message.content}"`);
            }

            sendMessage();
        });
    };

    sendMessage();
}, (error) => {
    console.error('❌ Erreur de connexion WebSocket :', error);
});
