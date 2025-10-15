package main

import (
	"fmt"
	"log"
	"net/http"
)

func main() {
	// 静态文件服务
	fs := http.FileServer(http.Dir("."))
	http.Handle("/", fs)

	// 启动服务器
	fmt.Println("HTTP server started on :8085")
	fmt.Println("Visit http://localhost:8085/test_websocket.html to test WebSocket")
	log.Fatal(http.ListenAndServe(":8085", nil))
}