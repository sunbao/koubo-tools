#!/usr/bin/env python3
# -*- coding: utf-8 -*-

from http.server import HTTPServer, BaseHTTPRequestHandler
import json
import urllib.parse
import random
import time

# 模拟数据
users = {
    "1": {
        "userId": 1,
        "username": "testuser",
        "broadcastType": "娱乐",
        "accountStyle": "幽默风趣",
        "platformSettings": "抖音",
        "createdAt": "2023-01-01T00:00:00"
    }
}

hotspots = [
    {
        "hotId": 1,
        "title": "AI技术发展趋势",
        "content": "AI技术在各行业的应用越来越广泛，特别是在内容创作领域。",
        "platform": "抖音",
        "heatIndex": 95.5,
        "tags": ["AI", "科技", "未来"],
        "type": "科技",
        "createdAt": "2023-01-01T00:00:00"
    },
    {
        "hotId": 2,
        "title": "夏日美食推荐",
        "content": "炎炎夏日，来点清爽美食，冰镇西瓜和冷面是不错的选择。",
        "platform": "快手",
        "heatIndex": 88.2,
        "tags": ["美食", "夏日", "清爽"],
        "type": "美食",
        "createdAt": "2023-01-01T00:00:00"
    },
    {
        "hotId": 3,
        "title": "旅行攻略分享",
        "content": "国内热门旅游景点推荐，云南大理和桂林山水是夏日避暑的好去处。",
        "platform": "抖音",
        "heatIndex": 92.1,
        "tags": ["旅行", "攻略", "景点"],
        "type": "旅游",
        "createdAt": "2023-01-01T00:00:00"
    }
]

scripts = [
    {
        "scriptId": 1,
        "title": "AI技术发展趋势",
        "content": "大家好！今天我们来聊一个超级热门的话题：AI技术的发展趋势。这个话题真的太火了！很多人都在讨论。我个人觉得这个现象背后有很多值得思考的地方。如果你也对这个话题感兴趣，记得点赞关注，评论区告诉我你的看法！",
        "style": "幽默风趣",
        "platform": "抖音",
        "hotReference": 1,
        "status": "published",
        "createdAt": "2023-01-01T00:00:00"
    }
]

tokens = {
    "testtoken123": 1
}

class MockServer(BaseHTTPRequestHandler):
    def do_OPTIONS(self):
        self.send_response(200)
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS')
        self.send_header('Access-Control-Allow-Headers', 'Content-Type, Authorization')
        self.end_headers()
    
    def do_GET(self):
        self.send_response(200)
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Content-Type', 'application/json')
        self.end_headers()
        
        if self.path.startswith('/api/users/'):
            # 获取用户信息
            user_id = self.path.split('/')[-1]
            if user_id in users:
                self.wfile.write(json.dumps(users[user_id]).encode())
            else:
                self.wfile.write(json.dumps({"error": "User not found"}).encode())
        elif self.path == '/api/hotspots':
            # 获取所有热点
            self.wfile.write(json.dumps(hotspots).encode())
        elif self.path.startswith('/api/hotspots/'):
            # 获取特定热点
            hot_id = int(self.path.split('/')[-1])
            hotspot = next((h for h in hotspots if h["hotId"] == hot_id), None)
            if hotspot:
                self.wfile.write(json.dumps(hotspot).encode())
            else:
                self.wfile.write(json.dumps({"error": "Hotspot not found"}).encode())
        elif self.path == '/api/scripts':
            # 获取所有文案
            self.wfile.write(json.dumps(scripts).encode())
        elif self.path.startswith('/api/scripts/'):
            # 获取特定文案
            script_id = int(self.path.split('/')[-1])
            script = next((s for s in scripts if s["scriptId"] == script_id), None)
            if script:
                self.wfile.write(json.dumps(script).encode())
            else:
                self.wfile.write(json.dumps({"error": "Script not found"}).encode())
        elif self.path == '/api/teleprompter/status':
            # 获取提词器状态
            self.wfile.write(json.dumps("connected").encode())
        else:
            self.wfile.write(json.dumps({"message": "API endpoint not implemented"}).encode())
    
    def do_POST(self):
        self.send_response(200)
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Content-Type', 'application/json')
        self.end_headers()
        
        content_length = int(self.headers['Content-Length'])
        post_data = self.rfile.read(content_length)
        data = {}
        if post_data:
            data = json.loads(post_data.decode('utf-8'))
        
        if self.path == '/api/users/login':
            # 用户登录
            username = data.get('username', '')
            password = data.get('password', '')
            
            # 简单验证
            if username == 'testuser' and password == 'password':
                response = {
                    "user": users["1"],
                    "token": "testtoken123"
                }
                self.wfile.write(json.dumps(response).encode())
            else:
                self.send_response(401)
                self.wfile.write(json.dumps({"error": "Invalid username or password"}).encode())
        elif self.path == '/api/users/register':
            # 用户注册
            username = data.get('username', '')
            broadcastType = data.get('broadcastType', '娱乐')
            
            # 检查用户名是否已存在
            if username == 'testuser':
                self.send_response(400)
                self.wfile.write(json.dumps({"error": "Username already exists"}).encode())
            else:
                # 创建新用户
                new_user_id = str(len(users) + 1)
                new_user = {
                    "userId": len(users) + 1,
                    "username": username,
                    "broadcastType": broadcastType,
                    "accountStyle": "幽默风趣",
                    "platformSettings": "抖音",
                    "createdAt": time.strftime('%Y-%m-%dT%H:%M:%S')
                }
                users[new_user_id] = new_user
                response = {
                    "user": new_user,
                    "token": "testtoken" + new_user_id
                }
                self.wfile.write(json.dumps(response).encode())
        elif self.path == '/api/scripts/generate':
            # 生成文案
            user_id = data.get('userId', 1)
            hotspot_id = data.get('hotspotId', 1)
            
            # 模拟AI生成文案
            hotspot = next((h for h in hotspots if h["hotId"] == hotspot_id), hotspots[0])
            new_script = {
                "scriptId": len(scripts) + 1,
                "title": hotspot["title"],
                "content": f"根据热点'{hotspot['title']}'生成的AI口播文案内容。这是一个示例文案，展示AI如何根据热点和用户风格生成个性化内容。请根据实际情况调整语速和表达方式。",
                "style": "幽默风趣",
                "platform": hotspot["platform"],
                "hotReference": hotspot_id,
                "status": "published",
                "createdAt": time.strftime('%Y-%m-%dT%H:%M:%S')
            }
            scripts.append(new_script)
            self.wfile.write(json.dumps(new_script).encode())
        elif self.path == '/api/teleprompter/speed':
            # 计算最佳语速
            script_id = int(self.path.split('=')[1]) if '=' in self.path else 1
            speed = random.randint(180, 220)  # 随机生成180-220字/分钟的语速
            self.wfile.write(json.dumps(speed).encode())
        elif self.path == '/api/teleprompter/push':
            # 推送到提词器
            self.wfile.write(json.dumps("Script pushed to teleprompter successfully").encode())
        elif self.path == '/api/teleprompter/control':
            # 控制提词器
            command = data.get('command', '')
            self.wfile.write(json.dumps(f"Teleprompter command '{command}' executed successfully").encode())
        else:
            self.wfile.write(json.dumps({"message": "API endpoint not implemented"}).encode())
    
    def do_PUT(self):
        self.send_response(200)
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Content-Type', 'application/json')
        self.end_headers()
        
        content_length = int(self.headers['Content-Length'])
        put_data = self.rfile.read(content_length)
        data = {}
        if put_data:
            data = json.loads(put_data.decode('utf-8'))
        
        if self.path.startswith('/api/users/'):
            # 更新用户信息
            user_id = self.path.split('/')[-1]
            if user_id in users:
                # 更新用户信息
                user = users[user_id]
                if 'accountStyle' in data:
                    user['accountStyle'] = data['accountStyle']
                if 'platformSettings' in data:
                    user['platformSettings'] = data['platformSettings']
                users[user_id] = user
                self.wfile.write(json.dumps(user).encode())
            else:
                self.send_response(404)
                self.wfile.write(json.dumps({"error": "User not found"}).encode())
        else:
            self.wfile.write(json.dumps({"message": "API endpoint not implemented"}).encode())
    
    def do_DELETE(self):
        self.send_response(204)
        self.send_header('Access-Control-Allow-Origin', '*')
        self.end_headers()
        
        if self.path.startswith('/api/scripts/'):
            # 删除文案
            script_id = int(self.path.split('/')[-1])
            global scripts
            scripts = [s for s in scripts if s["scriptId"] != script_id]
            # 204状态码不需要返回内容

def run_mock_server():
    server_address = ('', 8080)
    httpd = HTTPServer(server_address, MockServer)
    print("Mock server running on port 8080")
    print("API endpoints available:")
    print("  GET  /api/users/{id}")
    print("  POST /api/users/login")
    print("  POST /api/users/register")
    print("  GET  /api/hotspots")
    print("  GET  /api/hotspots/{id}")
    print("  GET  /api/scripts")
    print("  GET  /api/scripts/{id}")
    print("  POST /api/scripts/generate")
    print("  POST /api/teleprompter/speed")
    print("  POST /api/teleprompter/push")
    print("  POST /api/teleprompter/control")
    print("  PUT  /api/users/{id}")
    print("  DELETE /api/scripts/{id}")
    print("\nPress Ctrl+C to stop the server")
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        print("\nServer stopped")

if __name__ == '__main__':
    run_mock_server()