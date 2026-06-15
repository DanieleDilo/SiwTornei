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
            <div className="card shadow-sm border-0 mb-4">
                <div className="card-body bg-white p-4 text-center">
                    <h2 className="text-success fw-bold mb-3">
                        <i className="bi bi-search"></i> Ricerca Squadre
                    </h2>
                    <input
                        type="text"
                        className="form-control form-control-lg border-success"
                        placeholder="Cerca squadra per nome o città..."
                        value={ricerca}
                        onChange={(e) => setRicerca(e.target.value)}
                    />
                   
                </div>
            </div>

            <div className="row g-4">
                {squadreFiltrate.length > 0 ? (
                    squadreFiltrate.map((squadra) => (
                        <div className="col-md-4" key={squadra.id}>
                            <div className="card h-100 border-success border-opacity-25 shadow-sm transition-hover">
                                <div className="card-body text-center">
                                    <h4 className="card-title fw-bold text-dark">{squadra.nome}</h4>
                                    <p className="text-muted mb-1"><i className="bi bi-geo-alt"></i> {squadra.citta}</p>
                                    <p className="text-muted"><i className="bi bi-calendar-star"></i> Fondazione: {squadra.annoFondazione}</p>
                                    <a href={"/squadra/" + squadra.id} className="btn btn-outline-success w-100">Profilo Squadra</a>
                                </div>
                            </div>
                        </div>
                    ))
                ) : (
                    <div className="col-12 text-center p-5">
                        <p className="text-muted fs-5">Nessuna squadra trovata con questi criteri.</p>
                    </div>
                )}
            </div>
        </div>
    );
}

const root = ReactDOM.createRoot(document.getElementById("react-root"));
root.render(<TeamSearch />);