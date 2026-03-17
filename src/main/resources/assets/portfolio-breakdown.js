document.addEventListener('DOMContentLoaded', () => {
    let rawHoldings = [];
    let cashBalance = 0;
    let enrichedData = [];

    let currentTab = 'country';
    let selectedCategory = null;
    let chartInstance = null;

    // Distinct colors for top categories
    const chartColors = ['#003f5c', '#2f4b7c', '#665191', '#a05195', '#d45087', '#f95d6a', '#ff7c43', '#ffa600', '#999999'];

    init();

    async function init() {
        setupTabs();

        try {
            const response = await fetch('/api/portfolio');
            if (!response.ok) {
                throw new Error("Not logged in or error fetching portfolio");
            }

            const userData = await response.json();

            cashBalance = userData.cash || 0;
            rawHoldings = userData.holdings || [];

            await fetchMetadataAndEnrich();
            renderDashboard();

        } catch (error) {
            console.error('Error initializing dashboard:', error);
            cashBalance = 0;
            rawHoldings = [];
            renderDashboard();
        }
    }

    async function fetchMetadataAndEnrich() {
        if (rawHoldings.length === 0) return;

        const tickers = rawHoldings.map(h => h.ticker).join(',');

        try {
            const response = await fetch(`/api/equities/metadata?tickers=${tickers}`);
            const json = await response.json();
            const metaMap = {};

            if (json.data) {
                json.data.forEach(item => { metaMap[item.ticker] = item; });
            }

            enrichedData = rawHoldings.map(holding => {
                const currentValue = holding.shares * holding.currentPrice;
                const meta = metaMap[holding.ticker] || {};

                return {
                    ticker: holding.ticker,
                    shares: holding.shares,
                    currentValue: currentValue,
                    name: meta.name || 'Unknown',
                    country: meta.country || 'Uncategorized',
                    region: meta.region || 'Uncategorized',
                    sector: meta.sector || 'Uncategorized'
                };
            });
        } catch (error) {
            console.error('Error fetching metadata:', error);
        }
    }

    function setupTabs() {
        document.querySelectorAll('.tab-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
                e.target.classList.add('active');
                currentTab = e.target.dataset.tab;
                selectedCategory = null;
                renderDashboard();
            });
        });
    }

    function renderDashboard() {
        const totalInvested = enrichedData.reduce((sum, item) => sum + item.currentValue, 0);
        const totalPortfolio = totalInvested + cashBalance;

        document.getElementById('investedValue').innerText = `£${totalInvested.toLocaleString(undefined, {minimumFractionDigits: 2})}`;
        document.getElementById('cashBalance').innerText = `£${cashBalance.toLocaleString(undefined, {minimumFractionDigits: 2})}`;
        document.getElementById('totalPortfolio').innerText = `£${totalPortfolio.toLocaleString(undefined, {minimumFractionDigits: 2})}`;

        let groups = {};
        enrichedData.forEach(item => {
            const cat = item[currentTab];
            if (!groups[cat]) groups[cat] = { value: 0, items: [] };
            groups[cat].value += item.currentValue;
            groups[cat].items.push(item);
        });

        const threshold = totalInvested * 0.05;
        let finalCategories = [];
        let otherValue = 0;
        let otherItems = [];

        Object.keys(groups).forEach(cat => {
            if (groups[cat].value < threshold) {
                otherValue += groups[cat].value;
                otherItems.push(...groups[cat].items);
            } else {
                finalCategories.push({ label: cat, value: groups[cat].value, items: groups[cat].items });
            }
        });

        finalCategories.sort((a, b) => b.value - a.value);

        if (otherValue > 0) {
            finalCategories.push({ label: 'Other', value: otherValue, items: otherItems });
        }

        renderChart(finalCategories, totalInvested);
        renderTable(finalCategories, totalInvested, totalPortfolio);
    }

    function renderChart(categories, totalInvested) {
        const ctx = document.getElementById('portfolioPieChart').getContext('2d');
        const labels = categories.map(c => c.label);
        const dataValues = categories.map(c => c.value);

        if (chartInstance) {
            chartInstance.destroy();
        }

        chartInstance = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: labels,
                datasets: [{
                    data: dataValues,
                    backgroundColor: chartColors.slice(0, categories.length),
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: { position: 'bottom' },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const value = context.parsed;
                                const percentage = ((value / totalInvested) * 100).toFixed(1);
                                return ` £${value.toLocaleString()} (${percentage}%)`;
                            }
                        }
                    }
                },
                onClick: (event, elements) => {
                    if (elements.length > 0) {
                        const index = elements[0].index;
                        const clickedCategory = labels[index];

                        if (selectedCategory === clickedCategory) {
                            selectedCategory = null;
                        } else {
                            selectedCategory = clickedCategory;
                        }
                        renderTable(categories, totalInvested, totalInvested + cashBalance);
                    }
                }
            }
        });
    }

    function renderTable(categories, totalInvested, totalPortfolio) {
        const tbody = document.getElementById('equitiesTableBody');
        const tableTitle = document.getElementById('tableTitle');
        tbody.innerHTML = '';

        let displayItems = [];
        let categoryTotal = totalInvested;

        if (selectedCategory) {
            tableTitle.innerText = `Equities in: ${selectedCategory}`;
            const targetCat = categories.find(c => c.label === selectedCategory);
            if (targetCat) {
                displayItems = targetCat.items;
                categoryTotal = targetCat.value;
            }
        } else {
            tableTitle.innerText = 'All Equities';
            categories.forEach(c => displayItems.push(...c.items));
        }

        displayItems.sort((a, b) => b.currentValue - a.currentValue);

        displayItems.forEach(item => {
            const tr = document.createElement('tr');

            const parentCatObj = categories.find(c => c.items.includes(item));
            const catVal = parentCatObj ? parentCatObj.value : item.currentValue;

            const percOfCat = ((item.currentValue / catVal) * 100).toFixed(1);
            const percOfTotal = ((item.currentValue / totalPortfolio) * 100).toFixed(1);

            tr.innerHTML = `
                <td><strong>${item.ticker}</strong></td>
                <td>${item.name}</td>
                <td>${item.shares}</td>
                <td>£${item.currentValue.toLocaleString(undefined, {minimumFractionDigits: 2})}</td>
                <td>${percOfCat}%</td>
                <td>${percOfTotal}%</td>
            `;
            tbody.appendChild(tr);
        });
    }
});