<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Task Manager</title>
    <script defer src="script.js"></script>
    <style>
        * { box-sizing: border-box; }
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #0f172a;
            color: #ffffff;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }
        .box {
            background: #1e293b;
            padding: 24px;
            border-radius: 12px;
            width: 100%;
            max-width: 480px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
        }
        h2 {
            margin-top: 0;
            margin-bottom: 16px;
            text-align: center;
        }
        .input-row {
            display: flex;
            gap: 10px;
            margin-bottom: 18px;
        }
        input {
            flex: 1;
            padding: 10px 12px;
            border-radius: 8px;
            border: 1px solid #475569;
            outline: none;
        }
        button {
            padding: 10px 14px;
            background: #16a34a;
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
        }
        button:hover {
            background: #15803d;
        }
        ul {
            list-style: none;
            padding: 0;
            margin: 0;
        }
        li {
            margin-top: 10px;
            background: #334155;
            padding: 10px 12px;
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 10px;
        }
        .delete-btn {
            background: #dc2626;
            padding: 6px 10px;
        }
        .delete-btn:hover {
            background: #b91c1c;
        }
        .task-text {
            word-break: break-word;
        }
    </style>
</head>
<body>
<div class="box">
    <h2>Task Manager</h2>
    <div class="input-row">
        <input id="taskInput" type="text" placeholder="Enter a task" />
        <button type="button" onclick="addTask()">Add</button>
    </div>
    <ul id="list"></ul>
</div>
</body>
</html>
