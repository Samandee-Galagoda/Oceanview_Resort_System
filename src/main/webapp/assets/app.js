window.Oceanview = window.Oceanview || {};

Oceanview.base = window.__BASE__ || '';

Oceanview.api = (path, options = {}) => {
    const url = Oceanview.base + (path.startsWith('/') ? path : '/' + path);
    return fetch(url, { credentials: 'same-origin', ...options })
        .then(res => res.json().then(data => ({ status: res.status, data })));
};

Oceanview.formPost = (path, payload) => {
    const body = new URLSearchParams();
    Object.keys(payload || {}).forEach(k => body.append(k, payload[k] == null ? '' : payload[k]));
    return Oceanview.api(path, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
        body: body.toString()
    });
};

Oceanview.logout = async () => {
    await Oceanview.formPost('/api/logout', {});
    window.location.href = Oceanview.base + '/index.jsp';
};

Oceanview.modal = (() => {
    let overlay;
    let titleEl;
    let bodyEl;

    const ensure = () => {
        if (overlay) return;
        overlay = document.createElement('div');
        overlay.className = 'modal-overlay';
        overlay.innerHTML = `
          <div class="modal" role="dialog" aria-modal="true">
            <div class="modal-header">
              <div class="modal-title"></div>
              <button class="modal-close" type="button" aria-label="Close">×</button>
            </div>
            <div class="modal-body"></div>
          </div>
        `;
        document.body.appendChild(overlay);
        titleEl = overlay.querySelector('.modal-title');
        bodyEl = overlay.querySelector('.modal-body');

        const closeBtn = overlay.querySelector('.modal-close');
        closeBtn.addEventListener('click', () => Oceanview.modal.close());
        overlay.addEventListener('click', (e) => {
            if (e.target === overlay) Oceanview.modal.close();
        });
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape') Oceanview.modal.close();
        });
    };

    return {
        open: (title, html) => {
            ensure();
            titleEl.textContent = title || '';
            bodyEl.innerHTML = html || '';
            overlay.style.display = 'flex';
        },
        close: () => {
            if (!overlay) return;
            overlay.style.display = 'none';
        }
    };
})();
