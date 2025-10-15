package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"strconv"
	"strings"
	"time"
)

// 用户结构体
type User struct {
	UserID           int64  `json:"userId"`
	Username         string `json:"username"`
	BroadcastType    string `json:"broadcastType"`
	AccountStyle     string `json:"accountStyle"`
	PlatformSettings string `json:"platformSettings"`
	CreatedAt        string `json:"createdAt"`
}

// 热点结构体
type Hotspot struct {
	HotID     int64    `json:"hotId"`
	Title     string   `json:"title"`
	Content   string   `json:"content"`
	Platform  string   `json:"platform"`
	HeatIndex float64  `json:"heatIndex"`
	Tags      []string `json:"tags"`
	Type      string   `json:"type"`
	CreatedAt string   `json:"createdAt"`
}

// 文案结构体
type Script struct {
	ScriptID      int64  `json:"scriptId"`
	Title         string `json:"title"`
	Content       string `json:"content"`
	Style         string `json:"style"`
	Platform      string `json:"platform"`
	HotReference  int64  `json:"hotReference"`
	Status        string `json:"status"`
	CreatedAt     string `json:"createdAt"`
}

// 登录请求结构体
type LoginRequest struct {
	Username string `json:"username"`
	Password string `json:"password"`
}

// 注册请求结构体
type RegisterRequest struct {
	Username      string `json:"username"`
	Password      string `json:"password"`
	BroadcastType string `json:"broadcastType"`
}

// 模拟数据
var users = map[int64]User{
	1: {
		UserID:           1,
		Username:         "testuser",
		BroadcastType:    "娱乐",
		AccountStyle:     "幽默风趣",
		PlatformSettings: "抖音",
		CreatedAt:        "2023-01-01T00:00:00",
	},
}

var hotspots = []Hotspot{
	{
		HotID:     1,
		Title:     "AI技术发展趋势",
		Content:   "AI技术在各行业的应用越来越广泛，特别是在内容创作领域。",
		Platform:  "抖音",
		HeatIndex: 95.5,
		Tags:      []string{"AI", "科技", "未来"},
		Type:      "科技",
		CreatedAt: "2023-01-01T00:00:00",
	},
	{
		HotID:     2,
		Title:     "夏日美食推荐",
		Content:   "炎炎夏日，来点清爽美食，冰镇西瓜和冷面是不错的选择。",
		Platform:  "快手",
		HeatIndex: 88.2,
		Tags:      []string{"美食", "夏日", "清爽"},
		Type:      "美食",
		CreatedAt: "2023-01-01T00:00:00",
	},
	{
		HotID:     3,
		Title:     "旅行攻略分享",
		Content:   "国内热门旅游景点推荐，云南大理和桂林山水是夏日避暑的好去处。",
		Platform:  "抖音",
		HeatIndex: 92.1,
		Tags:      []string{"旅行", "攻略", "景点"},
		Type:      "旅游",
		CreatedAt: "2023-01-01T00:00:00",
	},
}

var scripts = []Script{
	{
		ScriptID:      1,
		Title:         "AI技术发展趋势",
		Content:       "大家好！今天我们来聊一个超级热门的话题：AI技术的发展趋势。这个话题真的太火了！很多人都在讨论。我个人觉得这个现象背后有很多值得思考的地方。如果你也对这个话题感兴趣，记得点赞关注，评论区告诉我你的看法！",
		Style:         "幽默风趣",
		Platform:      "抖音",
		HotReference:  1,
		Status:        "published",
		CreatedAt:     "2023-01-01T00:00:00",
	},
}

var tokens = map[string]int64{
	"testtoken123": 1,
}

// 设置CORS头
func setCORSHeaders(w http.ResponseWriter) {
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
	w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")
}

// 处理OPTIONS请求
func handleOptions(w http.ResponseWriter, r *http.Request) {
	setCORSHeaders(w)
	w.WriteHeader(http.StatusOK)
}

// 获取用户信息
func getUser(w http.ResponseWriter, r *http.Request) {
	setCORSHeaders(w)
	w.Header().Set("Content-Type", "application/json")

	// 解析用户ID
	path := strings.TrimPrefix(r.URL.Path, "/api/users/")
	userID, err := strconv.ParseInt(path, 10, 64)
	if err != nil {
		http.Error(w, `{"error": "Invalid user ID"}`, http.StatusBadRequest)
		return
	}

	// 查找用户
	user, exists := users[userID]
	if !exists {
		http.Error(w, `{"error": "User not found"}`, http.StatusNotFound)
		return
	}

	// 返回用户信息
	json.NewEncoder(w).Encode(user)
}

// 用户登录
func loginUser(w http.ResponseWriter, r *http.Request) {
	setCORSHeaders(w)
	w.Header().Set("Content-Type", "application/json")

	// 解析请求体
	var req LoginRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, `{"error": "Invalid request body"}`, http.StatusBadRequest)
		return
	}

	// 简单验证
	if req.Username == "testuser" && req.Password == "password" {
		response := map[string]interface{}{
			"user":  users[1],
			"token": "testtoken123",
		}
		json.NewEncoder(w).Encode(response)
		return
	}

	http.Error(w, `{"error": "Invalid username or password"}`, http.StatusUnauthorized)
}

// 用户注册
func registerUser(w http.ResponseWriter, r *http.Request) {
	setCORSHeaders(w)
	w.Header().Set("Content-Type", "application/json")

	// 解析请求体
	var req RegisterRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, `{"error": "Invalid request body"}`, http.StatusBadRequest)
		return
	}

	// 检查用户名是否已存在
	for _, user := range users {
		if user.Username == req.Username {
			http.Error(w, `{"error": "Username already exists"}`, http.StatusBadRequest)
			return
		}
	}

	// 创建新用户
	newUserID := int64(len(users) + 1)
	newUser := User{
		UserID:           newUserID,
		Username:         req.Username,
		BroadcastType:    req.BroadcastType,
		AccountStyle:     "幽默风趣",
		PlatformSettings: "抖音",
		CreatedAt:        time.Now().Format("2006-01-02T15:04:05"),
	}
	users[newUserID] = newUser

	response := map[string]interface{}{
		"user":  newUser,
		"token": "testtoken" + strconv.FormatInt(newUserID, 10),
	}
	json.NewEncoder(w).Encode(response)
}

// 获取所有热点
func getHotspots(w http.ResponseWriter, r *http.Request) {
	setCORSHeaders(w)
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(hotspots)
}

// 获取特定热点
func getHotspot(w http.ResponseWriter, r *http.Request) {
	setCORSHeaders(w)
	w.Header().Set("Content-Type", "application/json")

	// 解析热点ID
	path := strings.TrimPrefix(r.URL.Path, "/api/hotspots/")
	hotID, err := strconv.ParseInt(path, 10, 64)
	if err != nil {
		http.Error(w, `{"error": "Invalid hotspot ID"}`, http.StatusBadRequest)
		return
	}

	// 查找热点
	for _, hotspot := range hotspots {
		if hotspot.HotID == hotID {
			json.NewEncoder(w).Encode(hotspot)
			return
		}
	}

	http.Error(w, `{"error": "Hotspot not found"}`, http.StatusNotFound)
}

// 获取所有文案
func getScripts(w http.ResponseWriter, r *http.Request) {
	setCORSHeaders(w)
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(scripts)
}

// 获取特定文案
func getScript(w http.ResponseWriter, r *http.Request) {
	setCORSHeaders(w)
	w.Header().Set("Content-Type", "application/json")

	// 解析文案ID
	path := strings.TrimPrefix(r.URL.Path, "/api/scripts/")
	scriptID, err := strconv.ParseInt(path, 10, 64)
	if err != nil {
		http.Error(w, `{"error": "Invalid script ID"}`, http.StatusBadRequest)
		return
	}

	// 查找文案
	for _, script := range scripts {
		if script.ScriptID == scriptID {
			json.NewEncoder(w).Encode(script)
			return
		}
	}

	http.Error(w, `{"error": "Script not found"}`, http.StatusNotFound)
}

// 生成文案
func generateScript(w http.ResponseWriter, r *http.Request) {
	setCORSHeaders(w)
	w.Header().Set("Content-Type", "application/json")

	// 解析查询参数
	query := r.URL.Query()
	userID, _ := strconv.ParseInt(query.Get("userId"), 10, 64)
	hotspotID, _ := strconv.ParseInt(query.Get("hotspotId"), 10, 64)

	// 默认值
	if userID == 0 {
		userID = 1
	}
	if hotspotID == 0 {
		hotspotID = 1
	}

	// 查找热点
	var hotspot Hotspot
	found := false
	for _, h := range hotspots {
		if h.HotID == hotspotID {
			hotspot = h
			found = true
			break
		}
	}

	// 如果没找到热点，使用第一个
	if !found {
		hotspot = hotspots[0]
	}

	// 创建新文案
	newScript := Script{
		ScriptID:      int64(len(scripts) + 1),
		Title:         hotspot.Title,
		Content:       fmt.Sprintf("根据热点'%s'生成的AI口播文案内容。这是一个示例文案，展示AI如何根据热点和用户风格生成个性化内容。请根据实际情况调整语速和表达方式。", hotspot.Title),
		Style:         "幽默风趣",
		Platform:      hotspot.Platform,
		HotReference:  hotspotID,
		Status:        "published",
		CreatedAt:     time.Now().Format("2006-01-02T15:04:05"),
	}
	scripts = append(scripts, newScript)

	json.NewEncoder(w).Encode(newScript)
}

// 计算最佳语速
func calculateSpeed(w http.ResponseWriter, r *http.Request) {
	setCORSHeaders(w)
	w.Header().Set("Content-Type", "application/json")

	// 解析查询参数
	query := r.URL.Query()
	scriptID, _ := strconv.ParseInt(query.Get("scriptId"), 10, 64)

	// 默认值
	if scriptID == 0 {
		scriptID = 1
	}

	// 生成随机语速 (180-220字/分钟)
	speed := 180 + int64(len(scripts))%41 // 180-220之间的值
	json.NewEncoder(w).Encode(speed)
}

// 推送到提词器
func pushToTeleprompter(w http.ResponseWriter, r *http.Request) {
	setCORSHeaders(w)
	w.Header().Set("Content-Type", "application/json")

	json.NewEncoder(w).Encode("Script pushed to teleprompter successfully")
}

// 控制提词器
func controlTeleprompter(w http.ResponseWriter, r *http.Request) {
	setCORSHeaders(w)
	w.Header().Set("Content-Type", "application/json")

	query := r.URL.Query()
	command := query.Get("command")

	response := fmt.Sprintf("Teleprompter command '%s' executed successfully", command)
	json.NewEncoder(w).Encode(response)
}

// 获取提词器状态
func getTeleprompterStatus(w http.ResponseWriter, r *http.Request) {
	setCORSHeaders(w)
	w.Header().Set("Content-Type", "application/json")

	json.NewEncoder(w).Encode("connected")
}

// 更新用户信息
func updateUser(w http.ResponseWriter, r *http.Request) {
	setCORSHeaders(w)
	w.Header().Set("Content-Type", "application/json")

	// 解析用户ID
	path := strings.TrimPrefix(r.URL.Path, "/api/users/")
	userID, err := strconv.ParseInt(path, 10, 64)
	if err != nil {
		http.Error(w, `{"error": "Invalid user ID"}`, http.StatusBadRequest)
		return
	}

	// 查找用户
	user, exists := users[userID]
	if !exists {
		http.Error(w, `{"error": "User not found"}`, http.StatusNotFound)
		return
	}

	// 解析请求体
	var updates map[string]interface{}
	if err := json.NewDecoder(r.Body).Decode(&updates); err != nil {
		http.Error(w, `{"error": "Invalid request body"}`, http.StatusBadRequest)
		return
	}

	// 更新用户信息
	if accountStyle, ok := updates["accountStyle"]; ok {
		if style, ok := accountStyle.(string); ok {
			user.AccountStyle = style
		}
	}
	if platformSettings, ok := updates["platformSettings"]; ok {
		if settings, ok := platformSettings.(string); ok {
			user.PlatformSettings = settings
		}
	}
	users[userID] = user

	json.NewEncoder(w).Encode(user)
}

// 删除文案
func deleteScript(w http.ResponseWriter, r *http.Request) {
	setCORSHeaders(w)
	w.Header().Set("Content-Type", "application/json")

	// 解析文案ID
	path := strings.TrimPrefix(r.URL.Path, "/api/scripts/")
	scriptID, err := strconv.ParseInt(path, 10, 64)
	if err != nil {
		http.Error(w, `{"error": "Invalid script ID"}`, http.StatusBadRequest)
		return
	}

	// 删除文案
	for i, script := range scripts {
		if script.ScriptID == scriptID {
			scripts = append(scripts[:i], scripts[i+1:]...)
			break
		}
	}

	w.WriteHeader(http.StatusNoContent)
}

// 主函数
func main() {
	// 设置路由
	http.HandleFunc("/api/users/", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == "OPTIONS" {
			handleOptions(w, r)
			return
		}

		if strings.HasSuffix(r.URL.Path, "/") && r.URL.Path != "/api/users/" {
			// 去掉末尾的斜杠
			http.Redirect(w, r, strings.TrimSuffix(r.URL.Path, "/"), http.StatusMovedPermanently)
			return
		}

		switch r.Method {
		case http.MethodGet:
			getUser(w, r)
		case http.MethodPost:
			if r.URL.Path == "/api/users/login" {
				loginUser(w, r)
			} else if r.URL.Path == "/api/users/register" {
				registerUser(w, r)
			}
		case http.MethodPut:
			updateUser(w, r)
		default:
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		}
	})

	http.HandleFunc("/api/hotspots", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == "OPTIONS" {
			handleOptions(w, r)
			return
		}

		switch r.Method {
		case http.MethodGet:
			getHotspots(w, r)
		default:
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		}
	})

	http.HandleFunc("/api/hotspots/", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == "OPTIONS" {
			handleOptions(w, r)
			return
		}

		switch r.Method {
		case http.MethodGet:
			getHotspot(w, r)
		default:
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		}
	})

	http.HandleFunc("/api/scripts/generate", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == "OPTIONS" {
			handleOptions(w, r)
			return
		}

		switch r.Method {
		case http.MethodPost:
			generateScript(w, r)
		default:
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		}
	})

	http.HandleFunc("/api/scripts", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == "OPTIONS" {
			handleOptions(w, r)
			return
		}

		switch r.Method {
		case http.MethodGet:
			getScripts(w, r)
		default:
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		}
	})

	http.HandleFunc("/api/scripts/", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == "OPTIONS" {
			handleOptions(w, r)
			return
		}

		switch r.Method {
		case http.MethodGet:
			getScript(w, r)
		case http.MethodDelete:
			deleteScript(w, r)
		default:
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		}
	})

	http.HandleFunc("/api/teleprompter/speed", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == "OPTIONS" {
			handleOptions(w, r)
			return
		}

		switch r.Method {
		case http.MethodPost:
			calculateSpeed(w, r)
		default:
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		}
	})

	http.HandleFunc("/api/teleprompter/push", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == "OPTIONS" {
			handleOptions(w, r)
			return
		}

		switch r.Method {
		case http.MethodPost:
			pushToTeleprompter(w, r)
		default:
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		}
	})

	http.HandleFunc("/api/teleprompter/control", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == "OPTIONS" {
			handleOptions(w, r)
			return
		}

		switch r.Method {
		case http.MethodPost:
			controlTeleprompter(w, r)
		default:
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		}
	})

	http.HandleFunc("/api/teleprompter/status", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == "OPTIONS" {
			handleOptions(w, r)
			return
		}

		switch r.Method {
		case http.MethodGet:
			getTeleprompterStatus(w, r)
		default:
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		}
	})

	// 启动服务器
	fmt.Println("Mock server running on port 8083")
	fmt.Println("API endpoints available:")
	fmt.Println("  GET  /api/users/{id}")
	fmt.Println("  POST /api/users/login")
	fmt.Println("  POST /api/users/register")
	fmt.Println("  GET  /api/hotspots")
	fmt.Println("  GET  /api/hotspots/{id}")
	fmt.Println("  GET  /api/scripts")
	fmt.Println("  GET  /api/scripts/{id}")
	fmt.Println("  POST /api/scripts/generate?userId={userId}&hotspotId={hotspotId}")
	fmt.Println("  POST /api/teleprompter/speed?scriptId={scriptId}")
	fmt.Println("  POST /api/teleprompter/push?scriptId={scriptId}")
	fmt.Println("  POST /api/teleprompter/control?command={command}")
	fmt.Println("  PUT  /api/users/{id}")
	fmt.Println("  DELETE /api/scripts/{id}")
	fmt.Println("\nPress Ctrl+C to stop the server")

	log.Fatal(http.ListenAndServe(":8083", nil))
}