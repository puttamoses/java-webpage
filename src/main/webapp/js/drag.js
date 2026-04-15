/* =============================================
   CARDER PUZZLE - Drag & Drop Engine
   Supports HTML5 drag/drop + touch events
   ============================================= */

(function () {
    'use strict';

    const grid  = document.getElementById('carGrid');
    let dragged = null;

    // ─── HTML5 Drag & Drop ─────────────────────────
    function initDragDrop() {
        grid.addEventListener('dragstart', e => {
            const card = e.target.closest('.car-card');
            if (!card) return;
            dragged = card;
            card.classList.add('dragging');
            e.dataTransfer.effectAllowed = 'move';
            e.dataTransfer.setData('text/plain', card.dataset.id);
        });

        grid.addEventListener('dragend', e => {
            const card = e.target.closest('.car-card');
            if (card) card.classList.remove('dragging');
            document.querySelectorAll('.drag-over').forEach(el => el.classList.remove('drag-over'));
            dragged = null;
            refreshNumbers();
        });

        grid.addEventListener('dragover', e => {
            e.preventDefault();
            e.dataTransfer.dropEffect = 'move';
            const target = e.target.closest('.car-card');
            if (target && target !== dragged) {
                document.querySelectorAll('.drag-over').forEach(el => el.classList.remove('drag-over'));
                target.classList.add('drag-over');
            }
        });

        grid.addEventListener('dragleave', e => {
            const target = e.target.closest('.car-card');
            if (target) target.classList.remove('drag-over');
        });

        grid.addEventListener('drop', e => {
            e.preventDefault();
            const target = e.target.closest('.car-card');
            if (!target || target === dragged || !dragged) return;
            target.classList.remove('drag-over');

            const allCards = [...grid.querySelectorAll('.car-card')];
            const fromIdx  = allCards.indexOf(dragged);
            const toIdx    = allCards.indexOf(target);

            if (fromIdx < toIdx) {
                target.after(dragged);
            } else {
                target.before(dragged);
            }

            refreshNumbers();
            animateSwap(dragged);
        });
    }

    // ─── Touch Drag & Drop ─────────────────────────
    function initTouchDrag() {
        let touchCard = null;
        let ghost = null;
        let startX, startY;

        grid.addEventListener('touchstart', e => {
            const card = e.target.closest('.car-card');
            if (!card) return;
            touchCard = card;
            const touch = e.touches[0];
            startX = touch.clientX;
            startY = touch.clientY;

            // Create ghost
            ghost = card.cloneNode(true);
            ghost.style.cssText = `
                position: fixed;
                left: ${card.getBoundingClientRect().left}px;
                top: ${card.getBoundingClientRect().top}px;
                width: ${card.offsetWidth}px;
                opacity: 0.85;
                z-index: 9998;
                pointer-events: none;
                border: 2px solid #e8001d;
                border-radius: 12px;
                box-shadow: 0 16px 40px rgba(0,0,0,0.7);
            `;
            document.body.appendChild(ghost);
            card.classList.add('dragging');
        }, { passive: true });

        grid.addEventListener('touchmove', e => {
            if (!touchCard || !ghost) return;
            e.preventDefault();
            const touch = e.touches[0];
            const dx = touch.clientX - startX;
            const dy = touch.clientY - startY;
            ghost.style.transform = `translate(${dx}px, ${dy}px)`;

            // Highlight target
            const el = document.elementFromPoint(touch.clientX, touch.clientY);
            const target = el && el.closest('.car-card');
            document.querySelectorAll('.drag-over').forEach(c => c.classList.remove('drag-over'));
            if (target && target !== touchCard) target.classList.add('drag-over');
        }, { passive: false });

        grid.addEventListener('touchend', e => {
            if (!touchCard) return;
            const touch = e.changedTouches[0];
            const el = document.elementFromPoint(touch.clientX, touch.clientY);
            const target = el && el.closest('.car-card');

            if (target && target !== touchCard) {
                const allCards = [...grid.querySelectorAll('.car-card')];
                const fromIdx  = allCards.indexOf(touchCard);
                const toIdx    = allCards.indexOf(target);
                if (fromIdx < toIdx) target.after(touchCard);
                else                 target.before(touchCard);
                animateSwap(touchCard);
            }

            touchCard.classList.remove('dragging');
            document.querySelectorAll('.drag-over').forEach(c => c.classList.remove('drag-over'));
            if (ghost) ghost.remove();
            ghost = null;
            touchCard = null;
            refreshNumbers();
        }, { passive: true });
    }

    // ─── Refresh position numbers ─────────────────
    function refreshNumbers() {
        const cards = grid.querySelectorAll('.car-card');
        cards.forEach((card, i) => {
            card.querySelector('.card-num').textContent = i + 1;
        });
        // Update hidden inputs for form submission
        const form = document.getElementById('orderForm');
        form.querySelectorAll('.order-input').forEach(inp => inp.remove());
        cards.forEach(card => {
            const inp = document.createElement('input');
            inp.type = 'hidden';
            inp.name = 'order[]';
            inp.value = card.dataset.id;
            inp.className = 'order-input';
            card.appendChild(inp);
        });
    }

    // ─── Swap animation ───────────────────────────
    function animateSwap(card) {
        card.style.transition = 'none';
        card.style.transform = 'scale(1.06) translateX(-8px)';
        requestAnimationFrame(() => {
            card.style.transition = 'transform 0.3s cubic-bezier(.34,1.56,.64,1)';
            card.style.transform = '';
        });
    }

    // ─── Leaderboard Fetch ─────────────────────────
    function loadLeaderboard() {
        const lbList = document.getElementById('lbList');
        if (!lbList) return;

        fetch('api/score')
            .then(r => r.json())
            .then(data => {
                if (!data.length) {
                    lbList.innerHTML = '<div class="lb-loading">No scores yet!</div>';
                    return;
                }
                lbList.innerHTML = data.map((entry, i) => `
                    <div class="lb-entry">
                        <div class="lb-rank">#${i + 1}</div>
                        <div>
                            <div class="lb-name">${escHtml(entry.name)}</div>
                            <div class="lb-meta">${escHtml(entry.mode)} · ${entry.moves} moves</div>
                        </div>
                        <div class="lb-score">${entry.score}</div>
                    </div>
                `).join('');
            })
            .catch(() => {
                lbList.innerHTML = '<div class="lb-loading">Offline mode</div>';
            });
    }

    function escHtml(str) {
        return String(str).replace(/[&<>"']/g, c =>
            ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
    }

    // ─── Init ─────────────────────────────────────
    initDragDrop();
    initTouchDrag();
    loadLeaderboard();
    setInterval(loadLeaderboard, 15000);

})();
