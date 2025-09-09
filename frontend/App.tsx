import React, { useState } from "react";
import { useAuth } from "./auth/useAuth";
import CandidatesList from "./pages/CandidatesList";
import "./styles/app.css";

const LoginBox: React.FC = () => {
  const { login } = useAuth();
  const [u, setU] = useState("hr");
  const [p, setP] = useState("secret");
  const [busy, setBusy] = useState(false);
  const [err, setErr] = useState<string | null>(null);

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    setBusy(true); setErr(null);
    try { await login(u, p); } catch(e:any) { setErr(e.message || "Login fehlgeschlagen"); }
    finally { setBusy(false); }
  };

  return (
    <form className="card form narrow" onSubmit={submit}>
      <h2>Anmelden</h2>
      <label>Benutzername <input value={u} onChange={e=>setU(e.target.value)} autoFocus /></label>
      <label>Passwort <input type="password" value={p} onChange={e=>setP(e.target.value)} /></label>
      {err && <div className="error">{err}</div>}
      <button className="btn" disabled={busy}>{busy ? "Anmelden..." : "Anmelden"}</button>
      <p className="hint">Tipp: `hr/secret`, `admin/secret` oder `agency/secret` (aus dem Backend-Seed)</p>
    </form>
  );
};

const App: React.FC = () => {
  const { token, roles, logout, apiBase } = useAuth();

  return (
    <div>
      <header className="topbar">
        <div className="brand">Chocadies Recruiting</div>
        <div className="spacer" />
        <div className="meta">
          <span className="muted">API: {apiBase}</span>
          {token ? (
            <>
              <span className="badge">Rollen: {roles.join(", ") || "—"}</span>
              <button className="btn" onClick={logout}>Logout</button>
            </>
          ) : null}
        </div>
      </header>

      <main className="main">
        {token ? <CandidatesList/> : <LoginBox/>}
      </main>
    </div>
  );
};

export default App;
