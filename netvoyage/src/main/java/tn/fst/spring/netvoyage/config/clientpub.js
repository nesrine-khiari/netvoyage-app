// import SockJS from "sockjs-client";
// import { Client } from "@stomp/stompjs";
//
// // Server URLs
// const SERVER_URL = "http://localhost:8088/ws"; // WebSocket endpoint
// const API_BASE_URL = "http://localhost:8088/api"; // Backend API
//
// // Simulated users
// const users = {
//     4: { numUser: 4, username: "Houcem Hbiri", email: "houcem96.hh@gmail.com", password: "password123",token:"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJob3VjZW05Ni5oaEBnbWFpbC5jb20iLCJpYXQiOjE3NDU4MzgzNTgsImV4cCI6MTc0NTkyNDc1OH0.xV3KKFHELvSB0OurU9g-exJjpBcALl7uQeN70yXxm6I" },
//     5: { numUser: 5, username: "Mariem Hbiri", email: "user2@example.com", password: "password456",token:"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJob3VjZW0uaGJpcmkxQGdtYWlsLmNvbSIsImlhdCI6MTc0NTgzODM3NCwiZXhwIjoxNzQ1OTI0Nzc0fQ.yBW8pe_5Ln-5ZTt7iPQ_RYZ9XOAoax0swDlREaU_Y6I" }
// };
//
// // Get userId from command-line argument
// const userId = process.argv[2] && users[process.argv[2]] ? parseInt(process.argv[2]) : 4;
// const currentUser = users[userId];
//
// if (!currentUser) {
//     console.error("❌ Invalid user ID. Use '4' or '5'.");
//     process.exit(1);
// }
//
// let stompClient;
//
// // Function to establish WebSocket connection
// function connectWebSocket(userId) {
//     const socket = new SockJS(SERVER_URL);
//     stompClient = new Client({
//         webSocketFactory: () => socket,
//         debug: (str) => console.log(str),
//         connectHeaders: {
//             Authorization: 'Bearer ' + currentUser.token   // ⬅️ Add this here!
//         },
//         onConnect: () => {
//             console.log(`✅ WebSocket connected for ${currentUser.username} (User ${userId})`);
//
//             // Subscribe to notifications for the user
//             stompClient.subscribe(`/topic/notifications/${userId}`, (message) => {
//                 const notification = JSON.parse(message.body);
//                 console.log(`🔔 Notification for ${currentUser.username}: ${notification.content}`);
//             });
//         },
//         onStompError: (frame) => console.error("❌ STOMP Error:", frame.headers["message"])
//     });
//
//     stompClient.activate();
// }
//
// // Function to add a publication
// async function addPublication(user) {
//     const response = await fetch(`${API_BASE_URL}/publications`, {
//         method: "POST",
//         headers: { "Content-Type": "application/json" , "Authorization":`Bearer ${currentUser.token}` },
//         body: JSON.stringify({
//             title: "My Last Publication",
//             content: "This is my publication content",
//             numOwner: userId
//         })
//     });
//
//     if (!response.ok) {
//         console.error("❌ Failed to add publication");
//         return null;
//     }
//
//     const publication = await response.json();
//
//
//     console.log(publication);
//     console.log(`📢 ${user.username} published: "${publication.title}"`);
//     return publication.numPublication;
// }
//
// // Function to add a comment
// async function addComment(user, publicationId) {
//     console.log(`👎 Adding a comment to publiction : ${publicationId} ..`);
//
//     const response = await fetch(`${API_BASE_URL}/publications/${publicationId}/comments`, {
//         method: "POST",
//         headers: { "Content-Type": "application/json","Authorization":`Bearer ${currentUser.token}` },
//         body: JSON.stringify({
//             numOwner: user.numUser,
//             content: "Great post!"
//         })
//     });
//
//     if (!response.ok) {
//         console.error("❌ Failed to add comment");
//         return;
//     }
//
//     const comment = await response.json();
//     console.log(`💬 ${user.username} commented: "${comment.content}"`);
//     return comment.numCommentaire;
// }
//
// async function likePublication(publicationId, userId) {
//     try {
//         console.log(`👎 Liking publication: ${publicationId} ..`);
//
//         const response = await fetch(`${API_BASE_URL}/publications/${publicationId}/like/${userId}`, {
//             method: "PUT",
//             headers: { "Content-Type": "application/json","Authorization":`Bearer ${currentUser.token}` }
//         });
//
//         if (!response.ok) {
//             throw new Error(`Error liking publication: ${response.statusText}`);
//         }
//
//         const result = await response.text();
//         console.log(`👍 ${result}`);
//     } catch (error) {
//         console.error("❌ Failed to like publication:", error);
//     }
// }
//
// async function dislikePublication(publicationId, userId) {
//     try {
//         console.log(`👎 Dislikiing publication: ${publicationId} ..`);
//         const response = await fetch(`${API_BASE_URL}/publications/${publicationId}/dislike/${userId}`, {
//             method: "PUT",
//             headers: { "Content-Type": "application/json","Authorization":`Bearer ${currentUser.token}` }
//         });
//
//         if (!response.ok) {
//             throw new Error(`Error disliking publication: ${response.statusText}`);
//         }
//
//         const result = await response.text();
//         console.log(`👎 ${result}`);
//     } catch (error) {
//         console.error("❌ Failed to dislike publication:", error);
//     }
// }
//
// // Function to like a comment
// async function likeComment(commentId, userId) {
//     try {
//         console.log(`👎 Liikiing comment: ${commentId} ..`);
//         const response = await fetch(`${API_BASE_URL}/commentaires/${commentId}/like/${userId}`, {
//             method: "PUT",
//             headers: { "Content-Type": "application/json","Authorization":`Bearer ${currentUser.token}` }
//         });
//
//         if (!response.ok) {
//             throw new Error(`Error liking comment: ${response.statusText}`);
//         }
//
//         const result = await response.text();
//         console.log(`👍 Comment Liked: ${result}`);
//     } catch (error) {
//         console.error("❌ Failed to like comment:", error);
//     }
// }
//
// // Function to dislike a comment
// async function dislikeComment(commentId, userId) {
//     try {
//
//         console.log(`👎 Dislikiing comment: ${commentId} ..`);
//         const response = await fetch(`${API_BASE_URL}/commentaires/${commentId}/dislike/${userId}`, {
//             method: "PUT",
//             headers: { "Content-Type": "application/json","Authorization":`Bearer ${currentUser.token}` }
//         });
//
//         if (!response.ok) {
//             throw new Error(`Error disliking comment: ${response.statusText}`);
//         }
//
//         const result = await response.text();
//         console.log(`👎 Comment Disliked: ${result}`);
//     } catch (error) {
//         console.error("❌ Failed to dislike comment:", error);
//     }
// }
//
// // Start the scenario
// (async function () {
//     console.log(`📡 Connecting WebSocket for ${currentUser.username}...`);
//     connectWebSocket(userId);
//
//
//
//     // Wait for WebSocket connection before proceeding
//     setTimeout(async () => {
//         if (userId === 4) {
//             var publicationId1;
//             var publicationId2;
//             console.log(`📝 User: ${currentUser.username} is creating a publication...`);
//              publicationId1 = await addPublication(currentUser);
//             if (publicationId1) {
//                 console.log(`📢 Publication created with ID: ${publicationId1}`);
//             }
//              publicationId2 = await addPublication(currentUser);
//             if (publicationId2) {
//                 console.log(`📢 Publication created with ID: ${publicationId2}`);
//             }
//             setTimeout(async () => {
//                 console.log(`👍 User ${userId} likes the comment 9 `);
//                 await likeComment(9, userId);
//
//                 setTimeout(async () => {
//                     console.log(`👎 User ${userId} dislikes the comment 10 `);
//                     await dislikeComment(10, userId);
//                 }, 5000); // Dislike after 2 seconds
//
//             }, 25000);
//         } else if (userId === 5) {
//             var commentId;
//
//             console.log(`⏳ Waiting for ${users[4].username} to create a publication...`);
//             setTimeout(async () => {
//
//                 console.log(`💬 ${currentUser.username} is adding a comment...`);
//                 commentId= await addComment(currentUser, 14);
//                 commentId= await addComment(currentUser, 15);
//             }, 5000); // Delay to ensure User1's publication exists
//             setTimeout(async () => {
//                 console.log(`👍 User ${currentUser.username} likes the publication ${14}  `);
//                 await likePublication(14, userId);
//
//                 setTimeout(async () => {
//                     console.log(`👎 User ${currentUser.username} dislikes the publication ${15} `);
//                     await dislikePublication(15, userId);
//                 }, 5000); // Dislike after 2 seconds
//
//             }, 10000);
//
//         }
//     }, 1000);
// })();
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import readline from "readline";

// Server URLs
const SERVER_URL = "http://localhost:8088/ws"; // WebSocket endpoint
const API_BASE_URL = "http://localhost:8088/api"; // Backend API

// Simulated users
const users = {
    4: { numUser: 4, username: "Houcem Hbiri", email: "houcem96.hh@gmail.com", password: "password123", token: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJob3VjZW05Ni5oaEBnbWFpbC5jb20iLCJyb2xlIjoiRU1QTE9ZRSIsImlhdCI6MTc0NTkxODY2NywiZXhwIjoxNzQ2MDA1MDY3fQ.operLux9gOvQcn8JsoAjgb0k1Urb-mxJGpu6Z9GIWrI" },
    5: { numUser: 5, username: "Mariem Hbiri", email: "user2@example.com", password: "password456", token: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJob3VjZW0uaGJpcmkxQGdtYWlsLmNvbSIsInJvbGUiOiJFTVBMT1lFIiwiaWF0IjoxNzQ1OTE4NjEzLCJleHAiOjE3NDYwMDUwMTN9.xiWNl4F65CkTD-MD3uRb1gN9lhitW6d_Vf9Oz4lrXPw" }
};

// Get userId from command-line argument
const userId = process.argv[2] && users[process.argv[2]] ? parseInt(process.argv[2]) : 4;
const currentUser = users[userId];

if (!currentUser) {
    console.error("❌ Invalid user ID. Use '4' or '5'.");
    process.exit(1);
}

// Readline interface for user input
const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

let stompClient;

// Function to establish WebSocket connection
function connectWebSocket(userId) {
    const socket = new SockJS(SERVER_URL);
    stompClient = new Client({
        webSocketFactory: () => socket,
        debug: (str) => console.log(str),
        connectHeaders: {
            Authorization: 'Bearer ' + currentUser.token
        },
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

// ========== ACTIONS ==========

// Function to add a publication
async function addPublication(user) {
    rl.question("Enter publication title: ", (title) => {
        rl.question("Enter publication content: ", async (content) => {
            const response = await fetch(`${API_BASE_URL}/publications`, {
                method: "POST",
                headers: { "Content-Type": "application/json", "Authorization": `Bearer ${currentUser.token}` },
                body: JSON.stringify({
                    title: title.trim(),
                    content: content.trim(),
                    numOwner: userId
                })
            });

            if (!response.ok) {
                console.error("❌ Failed to add publication");
                showMenu();
                return;
            }

            const publication = await response.json();
            console.log(`📢 ${user.username} published: "${publication.title}" (ID: ${publication.numPublication})`);
            showMenu();
        });
    });
}


// Function to add a comment
async function addComment(user, publicationId) {
    rl.question("Enter publication ID to comment on: ", (pubId) => {
        rl.question("Enter your comment content: ", async (commentContent) => {
            console.log(`adding new comment by userid : ${userId}`)
            const response = await fetch(`${API_BASE_URL}/publications/${parseInt(pubId.trim())}/comments`, {
                method: "POST",
                headers: { "Content-Type": "application/json", "Authorization": `Bearer ${currentUser.token}` },
                body: JSON.stringify({
                    numOwner: userId,
                    content: commentContent.trim()
                })
            });

            if (!response.ok) {
                console.error("❌ Failed to add comment");
                showMenu();
                return;
            }

            const comment = await response.json();
            console.log(`💬 ${currentUser.username} commented: "${comment.content}" (Comment ID: ${comment.numCommentaire})`);
            showMenu();
        });
    });
}

// Function to like a publication
async function likePublication(publicationId, userId) {
    const response = await fetch(`${API_BASE_URL}/publications/${publicationId}/like/${userId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json", "Authorization": `Bearer ${currentUser.token}` }
    });

    if (!response.ok) {
        console.error("❌ Failed to like publication");
        return;
    }

    const result = await response.text();
    console.log(`👍 Publication liked: ${result}`);
}

// Function to dislike a publication
async function dislikePublication(publicationId, userId) {
    const response = await fetch(`${API_BASE_URL}/publications/${publicationId}/dislike/${userId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json", "Authorization": `Bearer ${currentUser.token}` }
    });

    if (!response.ok) {
        console.error("❌ Failed to dislike publication");
        return;
    }

    const result = await response.text();
    console.log(`👎 Publication disliked: ${result}`);
}

// Function to like a comment
async function likeComment(commentId, userId) {
    const response = await fetch(`${API_BASE_URL}/commentaires/${commentId}/like/${userId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json", "Authorization": `Bearer ${currentUser.token}` }
    });

    if (!response.ok) {
        console.error("❌ Failed to like comment");
        return;
    }

    const result = await response.text();
    console.log(`👍 Comment liked: ${result}`);
}

// Function to dislike a comment
async function dislikeComment(commentId, userId) {
    const response = await fetch(`${API_BASE_URL}/commentaires/${commentId}/dislike/${userId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json", "Authorization": `Bearer ${currentUser.token}` }
    });

    if (!response.ok) {
        console.error("❌ Failed to dislike comment");
        return;
    }

    const result = await response.text();
    console.log(`👎 Comment disliked: ${result}`);
}

async function getStats() {
    const response = await fetch(`${API_BASE_URL}/statistics/user/${userId}`, {
        method: "GET",
        headers: { "Content-Type": "application/json", "Authorization": `Bearer ${currentUser.token}` }
    });

    if (!response.ok) {
        console.error("❌ Failed to get stats");
        return;
    }

    const stats = await response.text();
    console.log(`👎 Your Recent Activity: ${stats}`);
}

async function getCommentsSentiments() {
    const response = await fetch(`${API_BASE_URL}/commentaires/sentiments`, {
        method: "GET",
        headers: { "Content-Type": "application/json", "Authorization": `Bearer ${currentUser.token}` }
    });

    if (!response.ok) {
        console.error("❌ Failed to get stats");
        return;
    }

    const stats = await response.text();
    console.log(`👎  Comments Sentiment analysis: ${stats}`);
}

async function getCommentSentiment() {
    rl.question("Enter Comment ID to get sentiment of: ", async (commentID) => {
        const response = await fetch(`${API_BASE_URL}/commentaires/${commentID}/sentiment`, {
            method: "GET",
            headers: {"Content-Type": "application/json", "Authorization": `Bearer ${currentUser.token}`}
        });

        if (!response.ok) {
            console.error("❌ Failed to get stats");
            return;
        }

        const stats = await response.text();
        console.log(`👎 Your Comment ${commentID} Sentiment analysis: ${stats}`);
    });
}

// ========== MENU ==========

function showMenu() {
    console.log("\n🚀 Choose an action:");
    console.log("1. Create a publication");
    console.log("2. Comment on a publication");
    console.log("3. Like a publication");
    console.log("4. Dislike a publication");
    console.log("5. Like a comment");
    console.log("6. Dislike a comment");
    console.log("7. Get Your Recent Activity Stats");
    console.log("8. Get Comments Sentiment analysis");
    console.log("9. Get a specific comment sentiment analysis");
    console.log("10. Exit");

    rl.question("\nEnter your choice: ", async (choice) => {
        await handleUserChoice(choice.trim());
    });
}

async function handleUserChoice(choice) {
    switch (choice) {
        case "1":
            await addPublication(currentUser);
            break;
        case "2":
            rl.question("Enter publication ID to comment on: ", async (pubId) => {
                await addComment(currentUser, parseInt(pubId.trim()));
                showMenu();
            });
            return;
        case "3":
            rl.question("Enter publication ID to like: ", async (pubId) => {
                await likePublication(parseInt(pubId.trim()), userId);
                showMenu();
            });
            return;
        case "4":
            rl.question("Enter publication ID to dislike: ", async (pubId) => {
                await dislikePublication(parseInt(pubId.trim()), userId);
                showMenu();
            });
            return;
        case "5":
            rl.question("Enter comment ID to like: ", async (commentId) => {
                await likeComment(parseInt(commentId.trim()), userId);
                showMenu();
            });
            return;
        case "6":
            rl.question("Enter comment ID to dislike: ", async (commentId) => {
                await dislikeComment(parseInt(commentId.trim()), userId);
                showMenu();
            });
            return;
        case "7":
            await getStats();
            showMenu();
            return;
        case "8":
            await getCommentsSentiments();
            showMenu();
            return;
        case "9":
            await getCommentSentiment();
            showMenu();
            return;
        case "10":
            console.log("👋 Exiting... Goodbye!");
            rl.close();
            process.exit(0);
        default:
            console.log("❌ Invalid choice, please try again.");
    }
    // After action, show menu again
    showMenu();
}

// ========== START ==========

(async function () {
    console.log(`📡 Connecting WebSocket for ${currentUser.username}...`);
    connectWebSocket(userId);

    setTimeout(async () => {
        console.log(`✅ Ready! Welcome, ${currentUser.username}`);
        showMenu();
    }, 1000); // Wait for websocket to fully activate
})();
