import React, { useEffect, useState } from "react";
import { useAuth } from "../auth/useAuth";
import "../styles/app.css";

export type Candidate = {
  id?: string;
  firstName: string; lastName: string; email: string;
  phone?: string; location?: string; linkedinUrl?: string;
  skills: string[]; status: "NEW"|"SCREENING"|"INTERVIEW"|"OFFER"|"REJECTED";
};

type Props = {
  initial?: Candidate | null;
  onSaved?: () => void;
};

const empty: Candidate = {
  firstName: "", lastName: "", email: "", phone: "", location: "", linkedinUrl: "",
  skills: [], status: "NEW"
};

const CandidateForm: React.FC<Props> = ({ initial, onSaved }) => {
  const { apiFetch } = useAuth();
  const [model, setModel] = useState<Candidate>(empty);
  const [saving, setSaving] = useState(false);
  const isEdit = Boolean(initial?.id);

  useEffect(() => { setModel(initial ?? empty); }, [initial]);

  const update = (k: keyof Candidate, v: any) => setModel(m => ({...m, [k]: v}));
  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    try {
      if (isEdit) {
        const res = await apiFetch(`/api/v1/candidates/${initial!.id}`, {
          method: "PATCH",
          body: JSON.stringify({
            firstName: model.firstName,
            lastName: model.lastName,
            status: model.status,
            skills: model.skills,
            location: model.location,
            linkedinUrl: model.linkedinUrl,
            phone: model.phone
          })
        });
        if (!res.ok) throw new Error("Update fehlgeschlagen");
      } else {
        const res = await apiFetch(`/api/v1/candidates`, {
          method: "POST",
          body: JSON.stringify(model)
        });
        if (!res.ok) throw new Error("Erstellen fehlgeschlagen");
      }
      onSaved?.();
      setModel(empty);
    } catch (err:any) {
      alert(err.message || String(err));
    } finally {
      setSaving(false);
    }
  };

  return (
    <form className="card form" onSubmit={onSubmit}>
      <h3>{isEdit ? "Kandidat:in bearbeiten" : "Neue:n Kandidat:in anlegen"}</h3>
      <div className="grid">
        <label>Vorname
          <input value={model.firstName} onChange={e=>update("firstName", e.target.value)} required />
        </label>
        <label>Nachname
          <input value={model.lastName} onChange={e=>update("lastName", e.target.value)} required />
        </label>
        <label>E-Mail
          <input type="email" value={model.email} onChange={e=>update("email", e.target.value)} required disabled={isEdit}/>
        </label>
        <label>Telefon
          <input value={model.phone ?? ""} onChange={e=>update("phone", e.target.value)} />
        </label>
        <label>Ort
          <input value={model.location ?? ""} onChange={e=>update("location", e.target.value)} />
        </label>
        <label>LinkedIn
          <input value={model.linkedinUrl ?? ""} onChange={e=>update("linkedinUrl", e.target.value)} />
        </label>
        <label>Status
          <select value={model.status} onChange={e=>update("status", e.target.value as Candidate["status"])}>
            {["NEW","SCREENING","INTERVIEW","OFFER","REJECTED"].map(s => <option key={s} value={s}>{s}</option>)}
          </select>
        </label>
        <label>Skills (Komma-getrennt)
          <input value={model.skills.join(", ")} onChange={e=>update("skills", e.target.value.split(",").map(s=>s.trim()).filter(Boolean))} />
        </label>
      </div>
      <div className="right">
        <button className="btn" disabled={saving}>{saving ? "Speichern..." : "Speichern"}</button>
      </div>
    </form>
  );
};

export default CandidateForm;
