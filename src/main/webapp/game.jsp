<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>🏎️ Car Ordering Puzzle</title>
    <link rel="stylesheet" href="css/style.css">
    <link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&family=Rajdhani:wght@400;600;700&display=swap" rel="stylesheet">
</head>
<body>

<!-- HEADER -->
<header class="site-header">
    <div class="header-inner">
        <div class="logo">
            <span class="logo-icon">🏎️</span>
            <span class="logo-text">CARDER</span>
            <span class="logo-sub">PUZZLE</span>
        </div>
        <div class="stats-bar">
            <div class="stat"><span class="stat-label">MOVES</span><span class="stat-value" id="moveCount">${moves}</span></div>
            <div class="stat"><span class="stat-label">SCORE</span><span class="stat-value" id="scoreVal">${score}</span></div>
            <div class="stat mode-badge">
                <span class="stat-label">MODE</span>
                <span class="stat-value mode-val">${mode}</span>
            </div>
        </div>
    </div>
</header>

<!-- CONTROLS -->
<section class="controls-section">
    <div class="controls-inner">
        <div class="task-desc">
            <span class="task-icon">🎯</span>
            <c:choose>
                <c:when test="${mode == 'speed'}">Drag &amp; drop cars to order them <strong>FASTEST → SLOWEST</strong></c:when>
                <c:when test="${mode == 'price'}">Drag &amp; drop cars to order them <strong>MOST EXPENSIVE → CHEAPEST</strong></c:when>
                <c:otherwise>Drag &amp; drop cars to order them <strong>A → Z by name</strong></c:otherwise>
            </c:choose>
        </div>
        <div class="btn-group">
            <form method="post" action="game" style="display:inline">
                <input type="hidden" name="action" value="shuffle">
                <input type="hidden" name="mode" value="speed">
                <button type="submit" class="btn btn-speed">⚡ Speed</button>
            </form>
            <form method="post" action="game" style="display:inline">
                <input type="hidden" name="action" value="shuffle">
                <input type="hidden" name="mode" value="price">
                <button type="submit" class="btn btn-price">💰 Price</button>
            </form>
            <form method="post" action="game" style="display:inline">
                <input type="hidden" name="action" value="shuffle">
                <input type="hidden" name="mode" value="name">
                <button type="submit" class="btn btn-name">🔤 A-Z</button>
            </form>
        </div>
    </div>
</section>

<!-- RESULT BANNER -->
<c:if test="${correct != null}">
    <div class="result-banner ${correct ? 'banner-correct' : 'banner-wrong'}">
        <c:choose>
            <c:when test="${correct}">
                🏆 PERFECT ORDER! Score: <strong>${score}</strong> pts in ${moves} moves!
                <button class="btn-celebrate" onclick="fireworks()">🎆</button>
            </c:when>
            <c:otherwise>
                ❌ Not quite right — try again! Keep dragging!
            </c:otherwise>
        </c:choose>
    </div>
</c:if>

<!-- CAR GRID -->
<main class="game-area">
    <form method="post" action="game" id="orderForm">
        <input type="hidden" name="action" value="validate">

        <div class="car-grid" id="carGrid">
            <c:forEach var="car" items="${cards}" varStatus="status">
                <div class="car-card" draggable="true" data-id="${car.id}" data-speed="${car.speed}" data-price="${car.price}">
                    <div class="card-num">${status.index + 1}</div>

                    <!-- SVG Car Illustration -->
                    <div class="car-svg-wrap">
                        <svg viewBox="0 0 200 80" xmlns="http://www.w3.org/2000/svg" class="car-svg">
                            <!-- Shadow -->
                            <ellipse cx="100" cy="75" rx="75" ry="5" fill="rgba(0,0,0,0.25)"/>
                            <!-- Body -->
                            <rect x="15" y="38" width="170" height="28" rx="10" fill="${car.color}"/>
                            <!-- Cabin -->
                            <path d="M55 38 Q65 12 100 10 Q135 12 148 38 Z" fill="${car.color}" opacity="0.9"/>
                            <!-- Windshield -->
                            <path d="M62 38 Q70 18 100 16 Q125 18 138 38 Z" fill="#A8DAFF" opacity="0.75"/>
                            <!-- Hood line -->
                            <rect x="148" y="42" width="30" height="8" rx="4" fill="${car.color}" opacity="0.7"/>
                            <!-- Front bumper -->
                            <rect x="170" y="50" width="16" height="6" rx="3" fill="#DDD"/>
                            <!-- Rear bumper -->
                            <rect x="14" y="50" width="16" height="6" rx="3" fill="#DDD"/>
                            <!-- Headlight -->
                            <ellipse cx="181" cy="46" rx="6" ry="4" fill="#FFEF99"/>
                            <ellipse cx="181" cy="46" rx="4" ry="2.5" fill="#FFF"/>
                            <!-- Tail light -->
                            <ellipse cx="19" cy="46" rx="5" ry="3.5" fill="#FF4444"/>
                            <!-- Wheels -->
                            <circle cx="48"  cy="64" r="12" fill="#222"/>
                            <circle cx="48"  cy="64" r="7"  fill="#555"/>
                            <circle cx="48"  cy="64" r="3"  fill="#CCC"/>
                            <circle cx="152" cy="64" r="12" fill="#222"/>
                            <circle cx="152" cy="64" r="7"  fill="#555"/>
                            <circle cx="152" cy="64" r="3"  fill="#CCC"/>
                            <!-- Door line -->
                            <line x1="100" y1="40" x2="100" y2="62" stroke="rgba(0,0,0,0.2)" stroke-width="1.5"/>
                            <!-- Exhaust -->
                            <rect x="15" y="58" width="8" height="4" rx="2" fill="#999"/>
                            <!-- Racing stripe -->
                            <rect x="15" y="47" width="170" height="3" rx="1.5" fill="rgba(255,255,255,0.2)"/>
                        </svg>
                    </div>

                    <div class="car-info">
                        <div class="car-name">${car.name}</div>
                        <div class="car-specs">
                            <span class="spec spec-speed">⚡ <fmt:formatNumber value="${car.speed}"/> km/h</span>
                            <span class="spec spec-price">💰 $<fmt:formatNumber value="${car.price}" type="number"/></span>
                        </div>
                    </div>

                    <div class="drag-handle">⋮⋮</div>
                    <input type="hidden" name="order[]" value="${car.id}" class="order-input">
                </div>
            </c:forEach>
        </div>

        <div class="submit-row">
            <button type="submit" class="btn btn-check">✅ Check My Order</button>
        </div>
    </form>
</main>

<!-- LEADERBOARD SIDEBAR -->
<aside class="leaderboard">
    <h3 class="lb-title">🏆 Leaderboard</h3>
    <div id="lbList"><div class="lb-loading">Loading…</div></div>
</aside>

<script src="js/drag.js"></script>
<script src="js/fireworks.js"></script>
</body>
</html>
