import React from "react";
import "../styles/table.css";

type Candidate = {
  id?: string;
  firstName: string; lastName: string; email: string;
  phone?: string; location?: string; status: string;
  skills: string[]; linkedinUrl?: string;
};

type Props = {
  data: Candidate[];
  onSelect?: (c: Candidate) => void;
  onDelete?: (id: string) => void;
  canEdit?: boolean;
  canDelete?: boolean;
};

const CandidateTable: React.FC<Props> = ({ data, onSelect, onDelete, canEdit, canDelete }) => {
  return (
    <div className="table-wrap">
      <table className="tbl">
        <thead>
          <tr>
            <th>Name</th><th>E-Mail</th><th>Status</th><th>Skills</th><th>Ort</th><th>Aktion</th>
          </tr>
        </thead>
        <tbody>
          {data.map(c => (
            <tr key={c.id}>
              <td>{c.firstName} {c.lastName}</td>
              <td>{c.email}</td>
              <td><span className={`badge badge-${c.status.toLowerCase()}`}>{c.status}</span></td>
              <td>{c.skills?.join(", ")}</td>
              <td>{c.location ?? "-"}</td>
              <td className="actions">
                {canEdit && <button className="btn" onClick={() => onSelect?.(c)}>Bearbeiten</button>}
                {canDelete && c.id && <button className="btn danger" onClick={() => onDelete?.(c.id!)}>Löschen</button>}
              </td>
            </tr>
          ))}
          {data.length === 0 && (
            <tr><td colSpan={6} style={{textAlign:"center", opacity:.7}}>Keine Einträge</td></tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default CandidateTable;
