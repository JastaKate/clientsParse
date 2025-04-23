import { useState } from 'react';
import './App.css';

function App() {
  const [clients, setClients] = useState([]);
  const [street, setStreet] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchClients = () => {
    setLoading(true);
    setError(null);

    fetch(`/api/v1/clients?street=${encodeURIComponent(street)}`)
      .then((res) => {
        if (!res.ok) throw new Error('Ошибка при загрузке данных');
        return res.json();
      })
      .then((data) => {
        setClients(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error(err);
        setError(err.message);
        setLoading(false);
      });
  };
  return (
    <div className="App">
      <h1>Список клиентов по улице</h1>

     <div style={{ marginBottom: '1rem', display: 'flex', gap: '0.5rem' }}>
       <input
         type="text"
         placeholder="Введите улицу"
         value={street}
         onChange={(e) => setStreet(e.target.value)}
         style={{
           padding: '0.4rem 0.6rem',
           fontSize: '1rem',
           borderRadius: '4px',
           border: '1px solid #ccc',
           flex: '1',
         }}
       />
       <button
         onClick={fetchClients}
         style={{
           padding: '0.4rem 0.8rem',
           fontSize: '0.9rem',
           backgroundColor: '#007bff',
           color: '#fff',
           border: 'none',
           borderRadius: '4px',
           cursor: 'pointer',
         }}
       >
         Найти
       </button>
     </div>
      {loading && <p>Загрузка...</p>}
      {error && <p style={{ color: 'red' }}>Ошибка: {error}</p>}

      {!loading && !error && clients.length === 0 && <p>Нет данных</p>}

      {!loading && !error && clients.length > 0 && (
        <ul>
          {clients.map((client) => (
            <li key={client.id}>
              <strong>{client.name}</strong>
              <div>ID: {client.id}</div>
              <div>Персональный номер: {client.personalNumber}</div>
              <div>Адрес ID: {client.addressId}</div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default App;
