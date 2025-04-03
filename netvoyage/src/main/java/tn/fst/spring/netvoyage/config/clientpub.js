import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

// Server URLs
const SERVER_URL = "http://localhost:8088/ws"; // WebSocket endpoint
const API_BASE_URL = "http://localhost:8088/api"; // Backend API

// Simulated users
const users = {
    1: { numUser: 1, username: "User1", email: "user1@example.com", password: "password123" },
    2: { numUser: 2, username: "User2", email: "user2@example.com", password: "password456" }
};

// Get userId from command-line argument
const userId = process.argv[2] && users[process.argv[2]] ? parseInt(process.argv[2]) : 1;
const currentUser = users[userId];

if (!currentUser) {
    console.error("❌ Invalid user ID. Use '1' or '2'.");
    process.exit(1);
}

let stompClient;

// Function to establish WebSocket connection
function connectWebSocket(userId) {
    const socket = new SockJS(SERVER_URL);
    stompClient = new Client({
        webSocketFactory: () => socket,
        debug: (str) => console.log(str),
        onConnect: () => {
            console.log(`✅ WebSocket connected for ${currentUser.username} (User ${userId})`);

            // Subscribe to notifications for the user
            stompClient.subscribe(`/topic/notifications/${userId}`, (message) => {
                const notification = JSON.parse(message.body);
                console.log(`🔔 Notification for ${currentUser.username}: ${notification.content}`);
            });
        },
        onStompError: (frame) => console.error("❌ STOMP Error:", frame.headers["message"])
    });

    stompClient.activate();
}

// Function to add a publication
async function addPublication(user) {
    const response = await fetch(`${API_BASE_URL}/publications`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            title: "My Last Publication",
            content: "This is my publication content",
            numOwner: userId
        })
    });

    if (!response.ok) {
        console.error("❌ Failed to add publication");
        return null;
    }

    const publication = await response.json();


    console.log(publication);
    console.log(`📢 ${user.username} published: "${publication.title}"`);
    return publication.numPublication;
}

// Function to add a comment
async function addComment(user, publicationId) {
    const response = await fetch(`${API_BASE_URL}/publications/${publicationId}/comments`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            numOwner: user.numUser,
            content: "Great post!"
        })
    });

    if (!response.ok) {
        console.error("❌ Failed to add comment");
        return;
    }

    const comment = await response.json();
    console.log(`💬 ${user.username} commented: "${comment.content}"`);
}

// Start the scenario
(async function () {
    console.log(`📡 Connecting WebSocket for ${currentUser.username}...`);
    connectWebSocket(userId);

    // Wait for WebSocket connection before proceeding
    setTimeout(async () => {
        if (userId === 1) {
            console.log("📝 User1 is creating a publication...");
            const publicationId = await addPublication(currentUser);
            if (publicationId) {
                console.log(`📢 Publication created with ID: ${publicationId}`);
            }
        } else if (userId === 2) {
            console.log("⏳ Waiting for User1 to create a publication...");
            setTimeout(async () => {
                const publicationId = 13; // Manually set or fetch the latest publication ID
                console.log("💬 User2 is adding a comment...");
                await addComment(currentUser, publicationId);
            }, 5000); // Delay to ensure User1's publication exists
        }
    }, 1000);
})();
