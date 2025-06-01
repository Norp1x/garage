function startQRScanner() {
    const qr = new Html5Qrcode("qr-reader");
    qr.start({facingMode: "environment"}, {
        fps: 10,
        qrbox: 250
    }, (decodedText) => {
        alert("Zeskanowano: " + decodedText);
        qr.stop();
    });
}

document.getElementById('searchInput').addEventListener('input', function () {
    const query = this.value.toLowerCase();
    // tutaj powinieneś dodać logikę wyszukiwania z backendu
    console.log("Wyszukiwanie:", query);
});