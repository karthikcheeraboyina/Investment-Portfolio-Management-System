// App Logic for Investment Portfolio Management System
(function () {
    const pages = {
        login: document.getElementById('loginPage'),
        register: document.getElementById('registerPage'),
        dashboard: document.getElementById('dashboardPage'),
        portfolios: document.getElementById('portfoliosPage'),
        assets: document.getElementById('assetsPage'),
        performance: document.getElementById('performancePage'),
        risk: document.getElementById('riskPage'),
        reports: document.getElementById('reportsPage'),
        profile: document.getElementById('profilePage'),
    };

    const userNameSpan = document.getElementById('userName');
    
    const THEME_KEY = 'pm_theme';
    function applyTheme(theme) {
        const isDark = theme === 'dark';
        document.body.classList.toggle('theme-dark', isDark);
        document.documentElement.setAttribute('data-bs-theme', isDark ? 'dark' : 'light');
        const toggle = document.getElementById('themeToggle');
        if (toggle) toggle.checked = isDark;
        // Update chart defaults and re-render if present
        try {
            const gridColor = isDark ? 'rgba(255,255,255,0.1)' : 'rgba(0,0,0,0.1)';
            const tickColor = isDark ? '#cbd5e1' : '#495057';
            const titleColor = isDark ? '#e2e8f0' : '#212529';
            const plugin = Chart?.overrides?.line || {};
            if (Chart) {
                Chart.defaults.color = tickColor;
                Chart.defaults.borderColor = gridColor;
                Chart.defaults.plugins.legend.labels.color = tickColor;
                Chart.defaults.plugins.title.color = titleColor;
            }
            // Redraw charts on visible page
            if (document.getElementById('dashboardPage').style.display !== 'none') {
                loadDashboard();
            }
            if (document.getElementById('performancePage').style.display !== 'none') {
                loadPerformance();
            }
            if (document.getElementById('riskPage').style.display !== 'none') {
                loadRisk();
            }
        } catch (_) {}
        // Refresh animations for theme repaint
        if (typeof refreshAOS === 'function') refreshAOS();
    }

    window.toggleTheme = function () {
        const current = localStorage.getItem(THEME_KEY) || 'light';
        const next = current === 'dark' ? 'light' : 'dark';
        localStorage.setItem(THEME_KEY, next);
        applyTheme(next);
        return false;
    };

    function hideAllPages() {
        Object.values(pages).forEach(p => p && (p.style.display = 'none'));
    }

    function refreshAOS() {
        if (window.AOS && typeof window.AOS.refreshHard === 'function') {
            window.AOS.refreshHard();
        } else if (window.AOS && typeof window.AOS.refresh === 'function') {
            window.AOS.refresh();
        }
    }

    window.showPage = function (pageKey) {
        hideAllPages();
        switch (pageKey) {
            case 'dashboard':
                pages.dashboard.style.display = 'block';
                loadDashboard().finally(refreshAOS);
                break;
            case 'portfolios':
                pages.portfolios.style.display = 'block';
                loadPortfolios().finally(refreshAOS);
                break;
            case 'assets':
                pages.assets.style.display = 'block';
                loadAssets().finally(refreshAOS);
                break;
            case 'performance':
                pages.performance.style.display = 'block';
                loadPerformance().finally(refreshAOS);
                break;
            case 'risk':
                pages.risk.style.display = 'block';
                loadRisk().finally(refreshAOS);
                break;
            case 'reports':
                pages.reports.style.display = 'block';
                loadReports().finally(refreshAOS);
                break;
            case 'profile':
                pages.profile.style.display = 'block';
                loadProfile();
                refreshAOS();
                break;
            default:
                pages.login.style.display = 'block';
        }
        return false;
    };

    window.showLogin = function () {
        hideAllPages();
        pages.login.style.display = 'block';
        return false;
    };

    window.showRegister = function () {
        hideAllPages();
        pages.register.style.display = 'block';
        return false;
    };

    // Demo data seeding if user has no portfolios
    async function seedDemoIfNeeded() {
        const user = window.apiService.getCurrentUser();
        if (!user) return;
        const flagKey = `demoSeeded_${user.userId}`;
        if (localStorage.getItem(flagKey)) return;
        try {
            const existing = await window.apiService.getUserPortfolios(user.userId);
            if (existing && existing.length > 0) {
                localStorage.setItem(flagKey, '1');
                return;
            }
            const demoPortfolio = await window.apiService.createPortfolio({
                portfolioName: 'Demo Portfolio',
                description: 'Sample portfolio created for demo purposes'
            }, user.userId);

            const id = demoPortfolio.portfolioId;
            await window.apiService.addAssetToPortfolio(id, {
                assetName: 'Apple Inc.',
                symbol: 'AAPL',
                assetType: 'STOCK',
                quantity: 10,
                purchasePrice: 150,
                currentPrice: 175
            });
            await window.apiService.addAssetToPortfolio(id, {
                assetName: 'Vanguard S&P 500 ETF',
                symbol: 'VOO',
                assetType: 'ETF',
                quantity: 5,
                purchasePrice: 400,
                currentPrice: 450
            });
            await window.apiService.addAssetToPortfolio(id, {
                assetName: 'US Treasury Note 10Y',
                symbol: 'UST10Y',
                assetType: 'BOND',
                quantity: 10,
                purchasePrice: 100,
                currentPrice: 102
            });

            // Try to compute initial risk (optional)
            try { await window.apiService.calculateRisk(id); } catch (_) {}

            localStorage.setItem(flagKey, '1');
        } catch (e) {
            // Silent: seeding is optional
            console.warn('Demo seed skipped:', e);
        }
    }

    // Demo storage helpers
    function demoKey(userId) { return `demoPortfolios_${userId}`; }
    function getDemoPortfolios(userId) {
        try {
            const raw = localStorage.getItem(demoKey(userId));
            return raw ? JSON.parse(raw) : [];
        } catch (_) {
            return [];
        }
    }
    function saveDemoPortfolios(userId, list) {
        localStorage.setItem(demoKey(userId), JSON.stringify(list));
    }
    function createDemoPortfolio(userId, name, description) {
        const list = getDemoPortfolios(userId);
        const now = new Date().toISOString();
        const demo = {
            portfolioId: Number(Date.now()),
            portfolioName: name,
            description: description || '',
            creationDate: now,
            lastUpdated: now,
            assets: [],
            risks: []
        };
        list.push(demo);
        saveDemoPortfolios(userId, list);
        return demo;
    }
    function addDemoAsset(userId, portfolioId, payload) {
        const list = getDemoPortfolios(userId);
        const idx = list.findIndex(p => String(p.portfolioId) === String(portfolioId));
        if (idx === -1) return;
        const asset = {
            ...payload,
            assetId: Number(Date.now()),
            performances: []
        };
        list[idx].assets = [...(list[idx].assets || []), asset];
        list[idx].lastUpdated = new Date().toISOString();
        saveDemoPortfolios(userId, list);
    }
    async function getAllUserPortfolios(userId) {
        const backend = await window.apiService.getUserPortfolios(userId).catch(() => []);
        const demo = getDemoPortfolios(userId);
        return [...backend, ...demo];
    }

    // Auth handlers
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');

    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const username = document.getElementById('loginUsername').value.trim();
        const password = document.getElementById('loginPassword').value.trim();
        try {
            const user = await window.apiService.loginUser(username, password);
            window.apiService.setCurrentUser(user);
            localStorage.removeItem('demoAuth');
            userNameSpan.textContent = user.username;
            await seedDemoIfNeeded();
            showPage('dashboard');
        } catch (err) {
            // Demo fallback: allow frontend navigation without backend auth
            const demoUser = {
                userId: Number(localStorage.getItem('demoAuth')) || Date.now(),
                username: username || 'demo',
                email: (username || 'demo') + '@demo.local',
                role: 'INVESTOR'
            };
            window.apiService.setCurrentUser(demoUser);
            localStorage.setItem('demoAuth', String(demoUser.userId));
            userNameSpan.textContent = demoUser.username;
            showPage('dashboard');
        }
    });

    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const userData = {
            username: document.getElementById('regUsername').value.trim(),
            email: document.getElementById('regEmail').value.trim(),
            password: document.getElementById('regPassword').value.trim(),
            role: document.getElementById('regRole').value
        };
        try {
            const user = await window.apiService.registerUser(userData);
            alert('Registration successful. You can now log in.');
            showLogin();
        } catch (err) {
            alert('Registration failed. Please try again.');
        }
    });

    window.logout = function () {
        window.apiService.clearCurrentUser();
        userNameSpan.textContent = 'User';
        showLogin();
        return false;
    };

    // Dashboard loaders
    async function loadDashboard() {
        const user = window.apiService.getCurrentUser();
        if (!user) return showLogin();
        userNameSpan.textContent = user.username;

        try {
            const portfolios = await getAllUserPortfolios(user.userId);
            document.getElementById('totalPortfolios').textContent = portfolios.length;
            const allAssets = portfolios.flatMap(p => (p.assets || []));
            document.getElementById('totalAssets').textContent = allAssets.length;

            const totalValue = allAssets.reduce((sum, a) => {
                const qty = Number(a.quantity || 0);
                const price = Number((a.currentPrice ?? a.purchasePrice) || 0);
                return sum + qty * price;
            }, 0);
            document.getElementById('totalValue').textContent = window.apiService.formatCurrency(totalValue);

            // Simple risk label from first portfolio risk
            const riskLevel = (portfolios[0]?.risks?.[0]?.riskLevel) || 'LOW';
            document.getElementById('avgRiskLevel').textContent = riskLevel.replace('_', ' ');

            // Recent portfolios list
            const container = document.getElementById('recentPortfolios');
            container.innerHTML = '';
            portfolios.slice(-5).reverse().forEach(p => {
                const div = document.createElement('div');
                div.className = 'mb-2';
                div.innerHTML = `<strong>${p.portfolioName}</strong><div class="text-muted">Updated ${window.apiService.formatDateTime(p.lastUpdated)}</div>`;
                container.appendChild(div);
            });

            renderDashboardPerformanceChart(portfolios);
        } catch (e) {
            console.error(e);
        }
    }

    let perfChart;
    function renderDashboardPerformanceChart(portfolios) {
        const ctx = document.getElementById('performanceChart');
        if (!ctx) return;
        const labels = portfolios.map(p => p.portfolioName);
        const values = portfolios.map(p => (p.assets || []).reduce((sum, a) => sum + Number(a.quantity || 0) * Number((a.currentPrice ?? a.purchasePrice) || 0), 0));
        if (perfChart) perfChart.destroy();
        perfChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels,
                datasets: [{
                    label: 'Portfolio Value',
                    backgroundColor: '#0d6efd',
                    data: values
                }]
            },
            options: { responsive: true, maintainAspectRatio: false }
        });
    }

    // Portfolios page
    window.showCreatePortfolioModal = function () {
        const modalEl = document.getElementById('createPortfolioModal');
        const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
        modal.show();
    };

    async function loadPortfolios() {
        const user = window.apiService.getCurrentUser();
        if (!user) return showLogin();
        const list = document.getElementById('portfoliosList');
        list.innerHTML = '';
        const demoUserId = Number(localStorage.getItem('demoAuth')) || user.userId;
        const portfolios = await getAllUserPortfolios(demoUserId);
        portfolios.forEach(p => {
            const col = document.createElement('div');
            col.className = 'col-md-4';
            const assetCount = (p.assets || []).length;
            col.innerHTML = `
                <div class="card mb-3 animate__animated animate__fadeInUp" data-aos="fade-up">
                    <div class="card-body">
                        <h5 class="card-title">${p.portfolioName}</h5>
                        <p class="card-text text-muted">${p.description || ''}</p>
                        <div class="d-flex justify-content-between">
                            <span>${assetCount} assets</span>
                            <button class="btn btn-sm btn-outline-danger" data-id="${p.portfolioId}">Delete</button>
                        </div>
                    </div>
                </div>`;
            list.appendChild(col);
            const btn = col.querySelector('button');
            btn.addEventListener('click', async () => {
                if (confirm('Delete this portfolio?')) {
                    try {
                        await window.apiService.deletePortfolio(p.portfolioId);
                    } catch (_) {
                        // If backend delete fails, try demo storage
                        const demoUserId = Number(localStorage.getItem('demoAuth')) || user.userId;
                        const d = getDemoPortfolios(demoUserId).filter(dp => String(dp.portfolioId) !== String(p.portfolioId));
                        saveDemoPortfolios(demoUserId, d);
                    }
                    loadPortfolios().finally(refreshAOS);
                }
            });
        });

        // populate portfolio select for asset modal
        populatePortfolioSelect(portfolios);
        refreshAOS();
    }

    function populatePortfolioSelect(portfolios) {
        const select = document.getElementById('assetPortfolio');
        select.innerHTML = '<option value="">Select Portfolio</option>';
        portfolios.forEach(p => {
            const opt = document.createElement('option');
            opt.value = p.portfolioId;
            opt.textContent = p.portfolioName;
            select.appendChild(opt);
        });
    }

    window.createPortfolio = async function () {
        const user = window.apiService.getCurrentUser();
        const name = document.getElementById('portfolioName').value.trim();
        const description = document.getElementById('portfolioDescription').value.trim();
        if (!name) return alert('Portfolio name is required');
        try {
            await window.apiService.createPortfolio({ portfolioName: name, description }, user.userId);
        } catch (e) {
            // Demo fallback create (ensure stable demo user id)
            const demoUserId = Number(localStorage.getItem('demoAuth')) || user.userId;
            createDemoPortfolio(demoUserId, name, description);
        }
        bootstrap.Modal.getInstance(document.getElementById('createPortfolioModal')).hide();
        document.getElementById('createPortfolioForm').reset();
        loadPortfolios();
    };

    // Assets page
    window.showAddAssetModal = function () {
        const modalEl = document.getElementById('addAssetModal');
        const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
        modal.show();
    };

    async function loadAssets() {
        const user = window.apiService.getCurrentUser();
        if (!user) return showLogin();
        const tableBody = document.querySelector('#assetsTable tbody');
        tableBody.innerHTML = '';
        const demoUserId = Number(localStorage.getItem('demoAuth')) || user.userId;
        const portfolios = await getAllUserPortfolios(demoUserId);
        const assets = portfolios.flatMap(p => (p.assets || []).map(a => ({ ...a, portfolioName: p.portfolioName })));
        assets.forEach(a => {
            const qty = Number(a.quantity || 0);
            const price = Number((a.currentPrice ?? a.purchasePrice) || 0);
            const value = qty * price;
            const tr = document.createElement('tr');
            tr.setAttribute('data-aos', 'fade-up');
            tr.className = 'animate__animated animate__fadeIn';
            tr.innerHTML = `
                <td>${a.assetName}</td>
                <td>${a.symbol}</td>
                <td>${a.assetType}</td>
                <td>${qty}</td>
                <td>${window.apiService.formatCurrency(a.purchasePrice)}</td>
                <td>${a.currentPrice != null ? window.apiService.formatCurrency(a.currentPrice) : '-'}</td>
                <td>${window.apiService.formatCurrency(value)}</td>
                <td><span class="text-muted">—</span></td>`;
            tableBody.appendChild(tr);
        });
        populatePortfolioSelect(portfolios);
        refreshAOS();
    }

    window.addAsset = async function () {
        const portfolioId = document.getElementById('assetPortfolio').value;
        const assetName = document.getElementById('assetName').value.trim();
        const symbol = document.getElementById('assetSymbol').value.trim();
        const assetType = document.getElementById('assetType').value;
        const quantityVal = document.getElementById('assetQuantity').value;
        const purchasePriceVal = document.getElementById('assetPurchasePrice').value;
        const currentPriceVal = document.getElementById('assetCurrentPrice').value;
        if (!portfolioId || !assetName || !symbol || !assetType || !quantityVal || !purchasePriceVal) {
            return alert('Please complete all required fields.');
        }
        const quantity = Number(quantityVal);
        const purchasePrice = Number(purchasePriceVal);
        const currentPrice = currentPriceVal === '' ? null : Number(currentPriceVal);
        const payload = { assetName, symbol, assetType, quantity, purchasePrice, currentPrice };
        try {
            await window.apiService.addAssetToPortfolio(portfolioId, payload);
            bootstrap.Modal.getInstance(document.getElementById('addAssetModal')).hide();
            document.getElementById('addAssetForm').reset();
            loadAssets();
        } catch (e) {
            // Demo fallback add asset
            const user = window.apiService.getCurrentUser();
            const demoUserId = Number(localStorage.getItem('demoAuth')) || user.userId;
            addDemoAsset(demoUserId, portfolioId, payload);
            bootstrap.Modal.getInstance(document.getElementById('addAssetModal')).hide();
            document.getElementById('addAssetForm').reset();
            loadAssets();
        }
    };

    // Performance page
    let portfolioPerfChart;
    let assetPerfChart;

    async function loadPerformance() {
        const user = window.apiService.getCurrentUser();
        if (!user) return showLogin();
        const demoUserId = Number(localStorage.getItem('demoAuth')) || user.userId;
        const portfolios = await getAllUserPortfolios(demoUserId);
        const first = portfolios[0];
        const asset = first?.assets?.[0];

        // Portfolio performance (time series list from backend)
        if (first) {
            const perf = await window.apiService.getPortfolioPerformance(first.portfolioId).catch(() => []);
            renderLineChart('portfolioPerformanceChart', 'Portfolio Return %', perf.map(p => p.calculationDate), perf.map(p => Number(p.profitLossPercentage || 0)), (c) => (portfolioPerfChart = c));
        }

        // Asset performance
        if (asset) {
            const hist = await window.apiService.getAssetPerformanceHistory(asset.assetId).catch(() => []);
            renderLineChart('assetPerformanceChart', 'Asset Return %', hist.map(p => p.calculationDate), hist.map(p => Number(p.profitLossPercentage || 0)), (c) => (assetPerfChart = c));
        }

        // Performance table (use first portfolio)
        const tbody = document.querySelector('#performanceTable tbody');
        tbody.innerHTML = '';
        (first?.assets || []).forEach(a => {
            const row = document.createElement('tr');
            row.setAttribute('data-aos', 'fade-up');
            const latest = (a.performances || []).slice(-1)[0];
            row.innerHTML = `
                <td>${latest ? window.apiService.formatDateTime(latest.calculationDate) : '-'}</td>
                <td>${a.assetName}</td>
                <td>${latest ? Number(latest.profitLossPercentage || 0).toFixed(2) + '%' : '-'}</td>
                <td>${latest ? window.apiService.formatCurrency(latest.currentValue) : '-'}</td>`;
            tbody.appendChild(row);
        });
        refreshAOS();
    }

    function renderLineChart(canvasId, label, labels, values, setRef) {
        const ctx = document.getElementById(canvasId);
        if (!ctx) return;
        const chart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels.map(d => window.apiService.formatDateTime(d)),
                datasets: [{
                    label,
                    data: values,
                    borderColor: '#0d6efd',
                    backgroundColor: 'rgba(13, 110, 253, 0.1)',
                    tension: 0.3
                }]
            },
            options: { responsive: true, maintainAspectRatio: false }
        });
        setRef(chart);
    }

    // Risk page
    let riskDistributionChart;
    async function loadRisk() {
        const user = window.apiService.getCurrentUser();
        if (!user) return showLogin();
        const demoUserId = Number(localStorage.getItem('demoAuth')) || user.userId;
        const portfolios = await getAllUserPortfolios(demoUserId);
        const first = portfolios[0];
        const riskSummaryDiv = document.getElementById('riskSummary');
        riskSummaryDiv.innerHTML = '';
        if (first) {
            const risk = await window.apiService.viewRiskAnalysis(first.portfolioId).catch(() => null);
            if (risk) {
                riskSummaryDiv.innerHTML = `
                    <div class="row animate__animated animate__fadeIn" data-aos="fade-up">
                        <div class="col-6"><strong>Risk Level:</strong> ${risk.riskLevel}</div>
                        <div class="col-6"><strong>Volatility:</strong> ${Number(risk.volatilityScore || 0).toFixed(2)}</div>
                        <div class="col-6 mt-2"><strong>Beta:</strong> ${Number(risk.betaValue || 0).toFixed(2)}</div>
                        <div class="col-6 mt-2"><strong>VaR:</strong> ${Number(risk.varValue || 0).toFixed(2)}</div>
                    </div>`;
            } else {
                riskSummaryDiv.textContent = 'No risk analysis available. Generate one from Reports.';
            }
        }

        // Distribution
        const allRisks = await window.apiService.getRisksByLevel('LOW').catch(() => [])
            .then(async lows => ({ lows, meds: await window.apiService.getRisksByLevel('MEDIUM').catch(() => []) }))
            .then(async prev => ({ ...prev, highs: await window.apiService.getRisksByLevel('HIGH').catch(() => []) }));
        const labels = ['LOW', 'MEDIUM', 'HIGH'];
        const values = [allRisks.lows.length, allRisks.meds.length, allRisks.highs.length];
        const ctx = document.getElementById('riskDistributionChart');
        if (ctx) {
            if (riskDistributionChart) riskDistributionChart.destroy();
            riskDistributionChart = new Chart(ctx, {
                type: 'doughnut',
                data: {
                    labels,
                    datasets: [{ data: values, backgroundColor: ['#198754', '#ffc107', '#dc3545'] }]
                },
                options: { responsive: true, maintainAspectRatio: false }
            });
        }

        // Risk table (history for first portfolio)
        const tbody = document.querySelector('#riskTable tbody');
        tbody.innerHTML = '';
        if (first) {
            const history = await window.apiService.getPortfolioRiskHistory(first.portfolioId).catch(() => []);
            history.forEach(r => {
                const tr = document.createElement('tr');
                tr.setAttribute('data-aos', 'fade-up');
                tr.innerHTML = `
                    <td>${first.portfolioName}</td>
                    <td>${r.riskLevel}</td>
                    <td>${Number(r.volatilityScore || 0).toFixed(2)}</td>
                    <td>${window.apiService.formatDateTime(r.analysisDate)}</td>
                    <td><span class="text-muted">—</span></td>`;
                tbody.appendChild(tr);
            });
        }
        refreshAOS();
    }

    // Reports page
    async function loadReports() {
        const user = window.apiService.getCurrentUser();
        if (!user) return showLogin();
        const demoUserId = Number(localStorage.getItem('demoAuth')) || user.userId;
        const portfolios = await getAllUserPortfolios(demoUserId);
        const first = portfolios[0];
        const tbody = document.querySelector('#reportsTable tbody');
        tbody.innerHTML = '';
        if (first) {
            const reports = await window.apiService.getPortfolioReports(first.portfolioId).catch(() => []);
            reports.forEach(r => {
                const tr = document.createElement('tr');
                tr.setAttribute('data-aos', 'fade-up');
                tr.innerHTML = `
                    <td>${r.reportTitle || '-'}</td>
                    <td>${r.reportType}</td>
                    <td>${window.apiService.formatDateTime(r.generatedDate)}</td>
                    <td><span class="text-muted">—</span></td>`;
                tbody.appendChild(tr);
            });
        }

        // Hook up buttons
        window.generateReport = async function (type) {
            const u = window.apiService.getCurrentUser();
            if (!u) return alert('Create a portfolio first.');
            const ps = await getAllUserPortfolios(u.userId);
            const firstP = ps[0];
            if (!firstP) return alert('Create a portfolio first.');
            try {
                if (type === 'portfolio') await window.apiService.generatePortfolioReport(firstP.portfolioId);
                else if (type === 'risk') await window.apiService.generateRiskReport(firstP.portfolioId);
                else if (type === 'asset-analysis') await window.apiService.generateAssetAnalysisReport(firstP.portfolioId);
                loadReports().finally(refreshAOS);
            } catch (e) {
                alert('Failed to generate report');
            }
        };
        refreshAOS();
    }

    // Profile page
    function loadProfile() {
        const user = window.apiService.getCurrentUser();
        if (!user) return showLogin();
        document.getElementById('profileUsername').value = user.username;
        document.getElementById('profileEmail').value = user.email;
        document.getElementById('profileRole').value = user.role;

        const form = document.getElementById('profileForm');
        form.onsubmit = async (e) => {
            e.preventDefault();
            try {
                const updated = await window.apiService.updateUserProfile(user.userId, {
                    ...user,
                    email: document.getElementById('profileEmail').value.trim()
                });
                window.apiService.setCurrentUser(updated);
                alert('Profile updated');
            } catch (e1) {
                alert('Failed to update profile');
            }
        };
    }

    // Bootstrap entry
    (async function init() {
        const user = window.apiService.getCurrentUser();
        // Theme bootstrap: saved preference or system
        const saved = localStorage.getItem(THEME_KEY);
        const prefersDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
        applyTheme(saved ? saved : (prefersDark ? 'dark' : 'light'));
        if (user) {
            userNameSpan.textContent = user.username;
            await seedDemoIfNeeded();
            showPage('dashboard');
        } else {
            showLogin();
        }
        // Ensure AOS has initial layout
        setTimeout(refreshAOS, 50);
    })();
})(); 