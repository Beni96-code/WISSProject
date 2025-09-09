import React, { useEffect, useMemo, useState } from "react";
import { useAuth } from "../auth/useAuth";
import CandidateTable from "../components/CandidateTable";
import CandidateForm, { Candidate } from "./CandidateForm";
import "../styles/app.css";

type Page<T> = { content: T[]; totalElements: number; number: number; size: number; };

const CandidatesList: React.FC = () => {
  const { apiFetch, roles } = useAuth();
  const [data, setData] = useState<Candidate[]>([]);
  const [loading, setLoading] = useState(false);
  const [status, setStatus] = useState<string>("");
  const [skill, setSkill] = useState<string>("");
  const [selected, setSelected] = useState<Candidate | null>(null);

  const canEdit = useMemo(() => roles.includes("HR") || roles.includes("ADMIN"), [roles]);
  const canDelete = useMemo(() => roles.includes("ADMIN"), [roles]);

  const load = async () => {
    setLoading(true);
    const params = new URLSearchParams();
    if (status) params.set("status", status);
    if (skill) params.set("skill", skill);
    const res = await apiFetch(`/api/v1/candidates?${params.toString()}`);
    if (!res.ok) { setLoading(false); throw new Error("Laden fehlgeschlagen"); }
    const page: Page<Candidate> = await res.json();
    setData(page.content ?? []);
    setLoading(false);
  };

  useEffect(() => { load().catch(e => alert(e.message || String(e))); /* eslint-disable-next-line */ }, []);

  const remove = async (id: string) => {
    if (!confirm("Diesen Eintrag wirklich löschen?")) return;
    const res = await apiFetch(`/api/v1/candidates/${id}`, { method: "DELETE" });
    if (!res.ok) { alert("Löschen fehlgeschlagen"); return; }
    await load();
  };

  return (
    <div className="container">
      <div className="card">
        <h2>Kandidat:innen</h2>
        <div className="filters">
          <select value={status} onChange={e=>setStatus(e.target.value)}>
            <option value="">Status (alle)</option>
            {["NEW","SCREENING","INTERVIEW","OFFER","REJECTED"].map(s => <option key={s} value={s}>{s}</option>)}
          </select>
          <input placeholder="Skill (z. B. Spring)" value={skill} onChange={e=>setSkill(e.target.value)} />
          <button className="btn" onClick={load} disabled={loading}>{loading ? "Lade..." : "Suchen"}</button>
          {canEdit && <button className="btn" onClick={()=>setSelected({firstName:"", lastName:"", email:"", status:"NEW", skills:[]})}>Neu</button>}
        </div>

        <CandidateTable
          data={data}
          canEdit={canEdit}
          canDelete={canDelete}
          onSelect={c => setSelected(c)}
          onDelete={remove}
        />
      </div>

      {canEdit && (
        <CandidateForm
          initial={selected}
          onSaved={async () => { setSelected(null); await load(); }}
        />
      )}
    </div>
  );
};

export default CandidatesList;


// Paste diesen Typ dorthin, wo du ihn brauchst (z. B. in CandidatesList.tsx exportieren):
export type Candidate = {
  id?: string;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  skills: string[];
  location?: string;
  linkedinUrl?: string;
  status: "NEW"|"SCREENING"|"INTERVIEW"|"OFFER"|"REJECTED";
  createdAt?: string;
  updatedAt?: string;
};
