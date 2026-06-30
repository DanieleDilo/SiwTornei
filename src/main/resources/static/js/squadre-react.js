// File: src/main/resources/static/js/squadre-react.js

function TeamSearch() {
    const [squadre, setSquadre] = React.useState([]);
    const [ricerca, setRicerca] = React.useState("");

    React.useEffect(() => {
        fetch("/api/squadre")
            .then(response => response.json())
            .then(data => setSquadre(data))
            .catch(error => console.error("Errore:", error));
    }, []);

    const squadreFiltrate = squadre.filter(s =>
        s.nome.toLowerCase().includes(ricerca.toLowerCase()) ||
        s.citta.toLowerCase().includes(ricerca.toLowerCase())
    );

    return (
        <div className="container">
            <a href="/" className="btn btn-outline-secondary mb-4 shadow-sm">
                <i className="bi bi-arrow-left"></i> Torna alla Home
            </a>
            
            <div className="row g-4">
                {/* Left Column: Search Form Card */}
                <div className="col-lg-4">
                    <div className="card shadow-sm border-0 mb-4 sticky-top" style={{ top: "100px", zIndex: 10 }}>
                        <div className="card-body p-4">
                            <h4 className="fw-bold text-white mb-3">
                                <i className="bi bi-funnel-fill"></i> Filtra Squadre
                            </h4>
                            <div className="mb-3">
                                <label className="form-label text-muted small text-uppercase fw-bold">Nome o Città</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    placeholder="Cerca squadra..."
                                    value={ricerca}
                                    onChange={(e) => setRicerca(e.target.value)}
                                />
                            </div>
                        </div>
                    </div>
                </div>
                
                {/* Right Column: Search Results Grid */}
                <div className="col-lg-8">
                    <div className="d-flex align-items-center justify-content-between mb-4">
                        <h2 className="text-success fw-bold mb-0">
                            <i className="bi bi-shield-shaded"></i> Ricerca Squadre
                        </h2>
                    </div>
                    
                    <div className="row g-4">
                        {squadreFiltrate.length > 0 ? (
                            squadreFiltrate.map((squadra) => (
                                <div className="col-md-6" key={squadra.id}>
                                    <div className="card h-100 border-success border-opacity-25 shadow-sm transition-hover">
                                        <div className="card-body text-center p-4 d-flex flex-column justify-content-between">
                                            <div>
                                                <div className="mb-3">
                                                    <i className="bi bi-shield-fill display-5 text-success"></i>
                                                </div>
                                                <h4 className="card-title fw-bold text-white mb-2">{squadra.nome}</h4>
                                                <p className="text-muted mb-1"><i className="bi bi-geo-alt"></i> {squadra.citta}</p>
                                                <p className="text-muted small mb-4"><i className="bi bi-calendar-star"></i> Fondazione: {squadra.annoFondazione}</p>
                                            </div>
                                            <a href={"/squadra/" + squadra.id} className="btn btn-outline-success w-100 fw-bold">
                                                Profilo Squadra <i className="bi bi-arrow-right"></i>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            ))
                        ) : (
                            <div className="col-12 text-center p-5">
                                <i className="bi bi-search text-muted display-3 mb-3"></i>
                                <p className="text-muted fs-5">Nessuna squadra trovata con questi criteri.</p>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}

const root = ReactDOM.createRoot(document.getElementById("react-root"));
root.render(<TeamSearch />);