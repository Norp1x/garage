class GarageToolManager {
    constructor() {
        this.apiUrl = '/api/v1/garage';
        this.qrScanner = null;
        this.init();
    }

    init() {
        this.setupEventListeners();
        this.loadTools();
    }

    setupEventListeners() {
        // Wyszukiwanie narzędzi
        const searchInput = document.getElementById('searchInput');
        if (searchInput) {
            let searchTimeout;
            searchInput.addEventListener('input', (e) => {
                clearTimeout(searchTimeout);
                searchTimeout = setTimeout(() => {
                    this.searchTools(e.target.value);
                }, 300);
            });
        }

        // Formularz dodawania narzędzia
        const toolForm = document.getElementById('toolForm');
        if (toolForm) {
            toolForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.addTool(new FormData(toolForm));
            });
        }

        // Przycisk skanowania QR
        const qrScanButton = document.getElementById('qrScanButton');
        if (qrScanButton) {
            qrScanButton.addEventListener('click', () => this.startQRScanner());
        }

        // Przycisk zatrzymania skanowania
        const stopScanButton = document.getElementById('stopScanButton');
        if (stopScanButton) {
            stopScanButton.addEventListener('click', () => this.stopQRScanner());
        }
    }

    async loadTools() {
        try {
            const response = await fetch(`${this.apiUrl}/tools`);
            const tools = await response.json();
            this.displayTools(tools);
        } catch (error) {
            console.error('Błąd podczas ładowania narzędzi:', error);
            this.showError('Nie udało się załadować narzędzi');
        }
    }

    async searchTools(query) {
        if (!query.trim()) {
            this.loadTools();
            return;
        }

        try {
            const response = await fetch(`${this.apiUrl}/tools/search?q=${encodeURIComponent(query)}`);
            const tools = await response.json();
            this.displayTools(tools);
        } catch (error) {
            console.error('Błąd podczas wyszukiwania:', error);
            this.showError('Błąd podczas wyszukiwania');
        }
    }

    displayTools(tools) {
        const resultsContainer = document.getElementById('results');
        if (!resultsContainer) return;

        if (tools.length === 0) {
            resultsContainer.innerHTML = '<p class="no-results">Nie znaleziono narzędzi</p>';
            return;
        }

        resultsContainer.innerHTML = tools.map(tool => `
            <div class="tool-card" data-tool-id="${tool.id}">
                <h3>${tool.name}</h3>
                <p><strong>Typ:</strong> ${tool.type}</p>
                <p><strong>Rozmiar:</strong> ${tool.size}</p>
                <p><strong>Kolor:</strong> ${tool.color || 'Nie podano'}</p>
                <p><strong>Ilość:</strong> ${tool.quantity}</p>
                <p><strong>Miejsce:</strong> ${tool.toolPlacing}</p>
                <p><strong>Opis:</strong> ${tool.description || 'Brak opisu'}</p>
                <div class="tool-actions">
                    <button onclick="garageManager.generateQRCode(${tool.id})" class="btn-primary">
                        Generuj QR
                    </button>
                    <button onclick="garageManager.editTool(${tool.id})" class="btn-secondary">
                        Edytuj
                    </button>
                    <button onclick="garageManager.deleteTool(${tool.id})" class="btn-danger">
                        Usuń
                    </button>
                </div>
            </div>
        `).join('');
    }

    async addTool(formData) {
        const toolData = {
            name: formData.get('name'),
            type: formData.get('type'),
            description: formData.get('description'),
            size: formData.get('size'),
            color: formData.get('color'),
            quantity: formData.get('quantity'),
            toolPlacing: formData.get('toolPlacing')
        };

        try {
            const response = await fetch(`${this.apiUrl}/tools`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(toolData)
            });

            if (response.ok) {
                this.showSuccess('Narzędzie zostało dodane');
                this.loadTools();
                document.getElementById('toolForm').reset();
            } else {
                const error = await response.json();
                this.showError(error.message || 'Błąd podczas dodawania narzędzia');
            }
        } catch (error) {
            console.error('Błąd:', error);
            this.showError('Błąd podczas dodawania narzędzia');
        }
    }

    async generateQRCode(toolId) {
        try {
            const response = await fetch(`${this.apiUrl}/tools/${toolId}/qr-code`, {
                method: 'POST'
            });

            if (response.ok) {
                const qrCode = await response.json();
                this.showQRCode(qrCode);
            } else {
                this.showError('Błąd podczas generowania kodu QR');
            }
        } catch (error) {
            console.error('Błąd:', error);
            this.showError('Błąd podczas generowania kodu QR');
        }
    }

    showQRCode(qrCode) {
        const modal = document.getElementById('qrModal');
        const qrImage = document.getElementById('qrImage');

        if (modal && qrImage) {
            qrImage.src = qrCode.image;
            qrImage.alt = qrCode.name;
            modal.style.display = 'block';
        }
    }

    startQRScanner() {
        const qrReader = document.getElementById('qr-reader');
        if (!qrReader) return;

        this.qrScanner = new Html5Qrcode("qr-reader");

        this.qrScanner.start(
            {facingMode: "environment"},
            {
                fps: 10,
                qrbox: {width: 250, height: 250}
            },
            (decodedText) => {
                this.handleQRCodeScan(decodedText);
            },
            (errorMessage) => {
                // Ignoruj błędy skanowania (normalne gdy nie ma QR w kadrze)
            }
        ).catch(err => {
            console.error('Błąd podczas uruchamiania skanera:', err);
            this.showError('Nie udało się uruchomić skanera QR');
        });

        // Pokaż/ukryj przyciski
        document.getElementById('qrScanButton').style.display = 'none';
        document.getElementById('stopScanButton').style.display = 'inline-block';
    }

    stopQRScanner() {
        if (this.qrScanner) {
            this.qrScanner.stop().then(() => {
                this.qrScanner = null;

                // Pokaż/ukryj przyciski
                document.getElementById('qrScanButton').style.display = 'inline-block';
                document.getElementById('stopScanButton').style.display = 'none';
            }).catch(err => {
                console.error('Błąd podczas zatrzymywania skanera:', err);
            });
        }
    }

    async handleQRCodeScan(decodedText) {
        this.stopQRScanner();

        try {
            // Próbuj sparsować jako JSON (narzędzie)
            const toolData = JSON.parse(decodedText);
            this.showToolInfo(toolData);
        } catch (error) {
            // Jeśli to nie JSON, pokaż surowy tekst
            this.showInfo(`Zeskanowano kod QR: ${decodedText}`);
        }
    }

    showToolInfo(tool) {
        const info = `
            Narzędzie: ${tool.name}
            Typ: ${tool.type}
            Rozmiar: ${tool.size}
            Miejsce: ${tool.toolPlacing}
            ${tool.description ? 'Opis: ' + tool.description : ''}
        `;

        this.showInfo(info, 'Informacje o narzędziu');
    }

    // Utility methods
    showSuccess(message) {
        this.showNotification(message, 'success');
    }

    showError(message) {
        this.showNotification(message, 'error');
    }

    showInfo(message, title = 'Informacja') {
        this.showNotification(message, 'info');
    }

    showNotification(message, type = 'info') {
        // Prosta implementacja powiadomień
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.textContent = message;

        document.body.appendChild(notification);

        setTimeout(() => {
            notification.remove();
        }, 5000);
    }

    async deleteTool(toolId) {
        if (!confirm('Czy na pewno chcesz usunąć to narzędzie?')) {
            return;
        }

        try {
            const response = await fetch(`${this.apiUrl}/tools/${toolId}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                this.showSuccess('Narzędzie zostało usunięte');
                this.loadTools();
            } else {
                this.showError('Błąd podczas usuwania narzędzia');
            }
        } catch (error) {
            console.error('Błąd:', error);
            this.showError('Błąd podczas usuwania narzędzia');
        }
    }
}

// Inicjalizuj aplikację po załadowaniu DOM
document.addEventListener('DOMContentLoaded', () => {
    window.garageManager = new GarageToolManager();
});
