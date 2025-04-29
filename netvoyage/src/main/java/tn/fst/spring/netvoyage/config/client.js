const SockJS = require('sockjs-client');
const Stomp = require('stompjs');
const readline = require('readline');

const socket = new SockJS(`http://localhost:8088/ws`);
const stompClient = Stomp.over(socket);

// Users
const voyageur1 = {
    numVoyageur: 1,
    email: "nesrine.khiari01@gmail.com",
    firstname: "Nesrine",
    lastname: "Khiari",
    phone: "+21690765432",
    token: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZXNyaW5lLmtoaWFyaTAxQGdtYWlsLmNvbSIsImlhdCI6MTc0NTgzMTUwNSwiZXhwIjoxNzQ1OTE3OTA1fQ.mx43nkhpcP0djkBwtf5q9pruOLBgzT9VQ_0jpKfSXeE"
};

const voyageur2 = {
    numVoyageur: 2,
    email: "nesrine890@gmail.com",
    firstname: "John",
    lastname: "Doe",
    phone: "+21698765432",
    token: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZXNyaW5lODkwQGdtYWlsLmNvbSIsImlhdCI6MTc0NTgzMDE3OCwiZXhwIjoxNzQ1OTE2NTc4fQ.xExjJo03QC1nXnxquTYB8HuPDruws0XGJE-wJy0TrVg"
};

const voyageur3 = {
    numVoyageur: 3,
    email: "yacoubi803@gmail.com",
    firstname: "Yosra",
    lastname: "Yacoubi",
    phone: "+21612345678",
    token: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ5YWNvdWJpODAzQGdtYWlsLmNvbSIsImlhdCI6MTc0NTgzMTUyNywiZXhwIjoxNzQ1OTE3OTI3fQ.g-mz2TUmQGaJd2NYQTDAfA5gnKjn4BKzxLiGR3Ir8iY"
};

// Choose user based on CLI argument
const userId = process.argv[2] === '1' ? 1 : (process.argv[2] === '2' ? 2 : 3);
const user = userId === 1 ? voyageur1 : (userId === 2 ? voyageur2 : voyageur3);

const discussionId = 1;
const token = user.token;

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

let lastReceivedMessage = null; // Store the last received message

stompClient.connect({
    Authorization: 'Bearer ' + token
}, () => {
    console.log(`✅ ${user.firstname} (${user.numVoyageur}) connecté au WebSocket`);

    // Subscribe to the discussion channel
    stompClient.subscribe(`/topic/discussion/${discussionId}`, (message) => {
        const receivedMessage = JSON.parse(message.body);
        console.log(`📩 ${user.firstname} a reçu : "${receivedMessage.content}" de ${receivedMessage.senderName}`);
        lastReceivedMessage = receivedMessage;
    });

    // Subscribe to the ban channel for this user
    stompClient.subscribe(`/topic/ban/${user.numVoyageur}`, (message) => {
        const banMessage = message.body;
        console.log(`🚫 Banni : ${banMessage}`);
        console.log("👋 Déconnexion forcée...");
        stompClient.disconnect(() => {
            rl.close();
            process.exit(0);
        });
    });

    const sendMessage = () => {
        rl.question("✍️ Tapez votre message ('read' pour marquer le dernier message comme lu, 'exit' pour quitter) : ", (input) => {
            if (input.toLowerCase() === 'exit') {
                console.log("👋 Déconnexion...");
                stompClient.disconnect(() => rl.close());
                process.exit(0);
            }

            if (input.toLowerCase() === 'read') {
                if (lastReceivedMessage) {
                    console.log(`numMessage: ${lastReceivedMessage.numMessage}`);
                    stompClient.send(`/app/message/read/${discussionId}`, {}, user.numVoyageur.toString());
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
