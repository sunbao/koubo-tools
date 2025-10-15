package main

import (
	"fmt"
	"log"
	"net/http"

	"github.com/gorilla/websocket"
)

var upgrader = websocket.Upgrader{
	CheckOrigin: func(r *http.Request) bool {
		return true // 允许所有来源
	},
}

// 存储所有连接的客户端
var clients = make(map[*websocket.Conn]bool)

// 广播消息通道
var broadcast = make(chan Message)

// 消息结构体
type Message struct {
	Data string `json:"data"`
}

func main() {
	// 静态文件服务
	fs := http.FileServer(http.Dir("."))
	http.Handle("/", fs)

	// WebSocket端点
	http.HandleFunc("/ws/teleprompter", handleWebSocket)

	// 启动消息处理goroutine
	go handleMessages()

	// 启动服务器
	fmt.Println("WebSocket server started on :8084")
	fmt.Println("WebSocket endpoint: ws://localhost:8084/ws/teleprompter")
	log.Fatal(http.ListenAndServe(":8084", nil))
}

func handleWebSocket(w http.ResponseWriter, r *http.Request) {
	// 升级HTTP连接为WebSocket连接
	ws, err := upgrader.Upgrade(w, r, nil)
	if err != nil {
		log.Printf("Error upgrading to WebSocket: %v", err)
		return
	}
	defer ws.Close()

	// 注册客户端
	clients[ws] = true
	log.Println("Client connected")

	for {
		// 读取消息
		var msg Message
		err := ws.ReadJSON(&msg)
		if err != nil {
			log.Printf("Error reading JSON: %v", err)
			delete(clients, ws)
			break
		}

		// 打印接收到的消息
		log.Printf("Received: %s", msg.Data)

		// 广播消息给所有客户端
		broadcast <- msg
	}
}

func handleMessages() {
	for {
		// 获取要广播的消息
		msg := <-broadcast

		// 发送给所有连接的客户端
		for client := range clients {
			err := client.WriteJSON(msg)
			if err != nil {
				log.Printf("Error writing JSON: %v", err)
				client.Close()
				delete(clients, client)
			}
		}
	}
}