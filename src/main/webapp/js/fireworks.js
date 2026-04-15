/* Fireworks canvas animation for correct answer */
function fireworks() {
    const canvas = document.createElement('canvas');
    canvas.id = 'fireworksCanvas';
    canvas.width  = window.innerWidth;
    canvas.height = window.innerHeight;
    document.body.appendChild(canvas);
    const ctx = canvas.getContext('2d');
    const particles = [];
    const colors = ['#e8001d','#f5a800','#ffffff','#00e676','#2196f3','#ff6b35'];

    for (let b = 0; b < 8; b++) {
        setTimeout(() => {
            const x = Math.random() * canvas.width;
            const y = Math.random() * canvas.height * 0.6;
            for (let i = 0; i < 60; i++) {
                const angle = (Math.PI * 2 / 60) * i;
                const speed = 2 + Math.random() * 5;
                particles.push({
                    x, y,
                    vx: Math.cos(angle) * speed,
                    vy: Math.sin(angle) * speed,
                    life: 1,
                    color: colors[Math.floor(Math.random() * colors.length)],
                    size: 2 + Math.random() * 3
                });
            }
        }, b * 300);
    }

    let frame;
    function animate() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        for (let i = particles.length - 1; i >= 0; i--) {
            const p = particles[i];
            p.x  += p.vx;
            p.y  += p.vy;
            p.vy += 0.08;
            p.life -= 0.015;
            if (p.life <= 0) { particles.splice(i, 1); continue; }
            ctx.globalAlpha = p.life;
            ctx.fillStyle = p.color;
            ctx.beginPath();
            ctx.arc(p.x, p.y, p.size, 0, Math.PI * 2);
            ctx.fill();
        }
        ctx.globalAlpha = 1;
        if (particles.length > 0) {
            frame = requestAnimationFrame(animate);
        } else {
            canvas.remove();
            cancelAnimationFrame(frame);
        }
    }
    animate();
}

// Auto-fire on page load if correct
document.addEventListener('DOMContentLoaded', () => {
    if (document.querySelector('.banner-correct')) {
        setTimeout(fireworks, 400);
    }
});
