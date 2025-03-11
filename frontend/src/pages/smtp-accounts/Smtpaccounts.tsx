import React, { useEffect, useState } from "react";
import axios from 'axios';
import Cookies from 'js-cookie';
import { parseCookies } from "nookies";
import '../smtp-accounts/Smtpaccounts.css';
import Header from '../../components/header/Header';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheck, faFilePen, faFilter, faTrash, faUserPlus, faUsers, faEnvelope, faServer, faX } from '@fortawesome/free-solid-svg-icons';

interface SmtpAccount {
  id: number;
  host: string;
  port: number;
  username: string;
  auth: boolean;
  starttls: boolean;
  sslTrust: string;
}

const Smtpaccounts = () => {
  const [id, setId] = useState<number | null>(null);
  const [host, setHost] = useState('');
  const [port, setPort] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [sslTrust, setSslTrust] = useState('');
  const [auth, setAuth] = useState(false);
  const [starttls, setStarttls] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [smtpAccounts, setSmtpAccounts] = useState<SmtpAccount[]>([]);
  const [showUpdateFields, setShowUpdateFields] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  // Function to fetch SMTP accounts
  const fetchSmtpAccounts = async () => {
    const cookies = parseCookies();
    const token = cookies["accessToken"];

    if (token) {
      setIsLoading(true);
      try {
        const response = await axios.get("http://localhost:8080/api/v1/smtpaccount/get", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setSmtpAccounts(response.data);
      } catch (error) {
        console.error("Erro ao buscar contas SMTP:", error);
        setError("Falha ao carregar contas SMTP. Tente novamente.");
      } finally {
        setIsLoading(false);
      }
    }
  };

  // Function to delete SMTP account
  const handleDeleteSmtpAccount = async (id: number) => {
    if (!confirm("Tem certeza de que deseja excluir esta conta SMTP?")) {
      return;
    }
    
    const token = Cookies.get('accessToken');

    if (!token) {
      setError("Usuário não autenticado. Por favor, faça login novamente.");
      return;
    }

    setIsLoading(true);
    try {
      await axios.delete(`http://localhost:8080/api/v1/smtpaccount/delete?id=${id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      setSuccess("Conta SMTP excluída com sucesso!");
      fetchSmtpAccounts();
    } catch (err) {
      console.error("Erro ao excluir conta SMTP:", err);
      setError("Falha ao excluir conta SMTP.");
    } finally {
      setIsLoading(false);
    }
  };

  // Function to create SMTP account
  const handleCreateSmtpAccount = async (e: React.FormEvent) => {
    e.preventDefault();
    const token = Cookies.get('accessToken');

    if (!token) {
      setError("Usuário não autenticado. Por favor, faça login novamente.");
      return;
    }

    setIsLoading(true);
    try {
      await axios.post("http://localhost:8080/api/v1/smtpaccount/create", {
        host,
        auth,
        starttls,
        port,
        username,
        password,
        sslTrust
      }, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json"
        }
      });

      setSuccess("Conta SMTP criada com sucesso!");
      fetchSmtpAccounts();
      
      // Clear form fields after successful creation
      setHost('');
      setPort('');
      setUsername('');
      setPassword('');
      setSslTrust('');
      setAuth(false);
      setStarttls(false);
    } catch (err) {
      console.error("Erro ao criar conta SMTP:", err);
      setError("Falha ao criar conta SMTP.");
    } finally {
      setIsLoading(false);
    }
  };

  // Function to fill update fields with selected account data
  const handleEditSmtpAccount = (account: SmtpAccount) => {
    setId(account.id);
    setHost(account.host);
    setPort(account.port.toString());
    setUsername(account.username);
    setSslTrust(account.sslTrust);
    setAuth(account.auth);
    setStarttls(account.starttls);
    setShowUpdateFields(true);
  };

  // Function to update SMTP account
  const handleUpdateSmtpAccount = async (e: React.FormEvent) => {
    e.preventDefault();
    const token = Cookies.get("accessToken");
  
    if (!token) {
      setError("Usuário não autenticado. Por favor, faça login novamente.");
      return;
    }
  
    setIsLoading(true);
    try {
      await axios.put(
        `http://localhost:8080/api/v1/smtpaccount/update?id=${id}`,
        {
          host,
          auth,
          starttls,
          port: parseInt(port),
          username,
          password,
          sslTrust,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );
  
      setSuccess("Conta SMTP atualizada com sucesso!");
      fetchSmtpAccounts();
      setShowUpdateFields(false);
  
      // Reset states after update
      setId(null);
      setHost("");
      setPort("");
      setUsername("");
      setPassword("");
      setSslTrust("");
      setAuth(false);
      setStarttls(false);
    } catch (err) {
      console.error("Erro ao atualizar conta SMTP:", err.response?.data || err.message);
      setError("Falha ao atualizar a conta SMTP: " + (err.response?.data?.message || err.message));
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchSmtpAccounts();

    // Timeout to clear error message
    const errorTimeout = setTimeout(() => {
      if (error) {
        setError(null);
      }
    }, 5000);

    // Timeout to clear success message
    const successTimeout = setTimeout(() => {
      if (success) {
        setSuccess(null);
      }
    }, 5000);

    return () => {
      clearTimeout(errorTimeout);
      clearTimeout(successTimeout);
    };
  }, [error, success]);

  return (
    <div className='container-smtp-account-principal'>
      <div style={{ position: "absolute", top: "5px", left: "5px" }}><Header /></div>

      {/* Alert messages */}
      {error && <div className='alert alert-danger'><FontAwesomeIcon icon={faX} style={{marginRight: "8px"}} /> {error}</div>}
      {success && <div className='alert alert-success'><FontAwesomeIcon icon={faCheck} style={{marginRight: "8px"}} /> {success}</div>}

      {/* Create SMTP account form */}
      <div className='container-smtp-account'>
        <h1 className='title-smtp-container'><FontAwesomeIcon icon={faUserPlus} /> Adicionar Conta SMTP</h1>
        
        <form onSubmit={handleCreateSmtpAccount} className='form-smtp'>
          <div className='mb-3'>
            <label className='form-label'>Host</label>
            <input 
              type='text' 
              className='form-control' 
              value={host} 
              onChange={(e) => setHost(e.target.value)} 
              placeholder='e.g., smtp.gmail.com' 
              required 
            />
          </div>
          <div className='mb-3'>
            <label className='form-label'>Port</label>
            <input 
              type='number' 
              className='form-control' 
              value={port} 
              onChange={(e) => setPort(e.target.value)} 
              placeholder='e.g., 587' 
              required 
            />
          </div>
          <div className='mb-3'>
            <label className='form-label'>Username</label>
            <input 
              type='email' 
              className='form-control' 
              value={username} 
              onChange={(e) => setUsername(e.target.value)} 
              placeholder='your.email@example.com' 
              required 
            />
          </div>
          <div className='mb-3'>
            <label className='form-label'>Password</label>
            <input 
              type='password' 
              className='form-control' 
              value={password} 
              onChange={(e) => setPassword(e.target.value)} 
              placeholder='Your password' 
              required 
            />
          </div>
          <div className='mb-3'>
            <label className='form-label'>SSL Trust</label>
            <input 
              type='text' 
              className='form-control' 
              value={sslTrust} 
              onChange={(e) => setSslTrust(e.target.value)} 
              placeholder='e.g., *' 
              required 
            />
          </div>
          <div className='form-check form-switch'>
            <input 
              className='form-check-input' 
              type='checkbox' 
              id='auth' 
              checked={auth} 
              onChange={(e) => setAuth(e.target.checked)} 
            />
            <label className='form-check-label' htmlFor='auth'>Authentication</label>
          </div>
          <div className='form-check form-switch'>
            <input 
              type='checkbox' 
              className='form-check-input' 
              id='starttls' 
              checked={starttls} 
              onChange={(e) => setStarttls(e.target.checked)} 
            />
            <label className='form-check-label' htmlFor='starttls'>STARTTLS</label>
          </div>
          <button 
            type='submit' 
            className='btn-create-smtp' 
            disabled={isLoading}
          >
            {isLoading ? 'Carregando...' : 'Salvar Conta SMTP'}
          </button>
        </form>
      </div>

      {/* SMTP accounts list */}
      <div className='container-smtp-account-two'>
        <h1 className='title-smtp-container'><FontAwesomeIcon icon={faServer} /> SMTP Accounts</h1>
        <div style={{ overflow: "auto", width: "100%", textAlign: "start", borderRadius: "8px" }}>
          {isLoading ? (
            <div className="text-center p-4">Loading accounts...</div>
          ) : smtpAccounts.length === 0 ? (
            <div className="text-center p-4">No SMTP accounts found</div>
          ) : (
            <table className="table table-striped tb-clients">
              <thead>
                <tr>
                  <th scope="col">ID</th>
                  <th scope="col">Host</th>
                  <th scope="col">Port</th>
                  <th scope="col">Username</th>
                  <th scope="col">Auth</th>
                  <th scope="col">STARTTLS</th>
                  <th scope="col">SSL Trust</th>
                  <th scope="col">Actions</th>
                </tr>
              </thead>
              <tbody>
                {smtpAccounts.map((account) => (
                  <tr key={account.id}>
                    <td>{account.id}</td>
                    <td>{account.host}</td>
                    <td>{account.port}</td>
                    <td>{account.username}</td>
                    <td>
                      <span className={`badge ${account.auth ? 'bg-success' : 'bg-secondary'}`}>
                        {account.auth ? "Enabled" : "Disabled"}
                      </span>
                    </td>
                    <td>
                      <span className={`badge ${account.starttls ? 'bg-success' : 'bg-secondary'}`}>
                        {account.starttls ? "Enabled" : "Disabled"}
                      </span>
                    </td>
                    <td>{account.sslTrust}</td>
                    <td>
                      <div className="actions-container">
                        <button 
                          onClick={() => handleEditSmtpAccount(account)} 
                          className='actions-clients-logs-update'
                          title="Edit account"
                        >
                          <FontAwesomeIcon icon={faFilePen} />
                        </button>
                        <button 
                          onClick={() => handleDeleteSmtpAccount(account.id)} 
                          className='actions-clients-logs-trash'
                          title="Delete account"
                        >
                          <FontAwesomeIcon icon={faTrash} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>

      {/* SMTP account update modal */}
      {showUpdateFields && (
        <>
          {/* Modal backdrop */}
          <div
            style={{
              position: "fixed",
              top: 0,
              left: 0,
              width: "100%",
              height: "100%",
              backgroundColor: "rgba(0, 0, 0, 0.5)",
              backdropFilter: "blur(3px)",
              zIndex: 1000,
            }}
            onClick={() => setShowUpdateFields(false)}
          ></div>

          {/* Update form modal */}
          <div className="update-modal" style={{ position: "fixed", top: "50%", left: "50%", transform: "translate(-50%, -50%)", zIndex: 1001 }}>
            <button
              onClick={() => setShowUpdateFields(false)}
              className="modal-close-btn"
              aria-label="Close"
            >
              &times;
            </button>

            <h3 style={{ fontWeight: 600, fontSize: "1.4rem", color: "#2563eb", marginBottom: "20px" }}>Update SMTP Account</h3>
            
            <form onSubmit={handleUpdateSmtpAccount} className="update-form-grid">
              <div className="mb-3">
                <label htmlFor="update-host" className="form-label">Host:</label>
                <input
                  type="text"
                  id="update-host"
                  className="form-control"
                  value={host}
                  onChange={(e) => setHost(e.target.value)}
                  required
                />
              </div>
              <div className="mb-3">
                <label htmlFor="update-port" className="form-label">Port:</label>
                <input
                  type="number"
                  id="update-port"
                  className="form-control"
                  value={port}
                  onChange={(e) => setPort(e.target.value)}
                  required
                />
              </div>
              <div className="mb-3">
                <label htmlFor="update-username" className="form-label">Username:</label>
                <input
                  type="text"
                  id="update-username"
                  className="form-control"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  required
                />
              </div>
              
              <div className="mb-3">
                <label htmlFor="update-sslTrust" className="form-label">SSL Trust:</label>
                <input
                  type="text"
                  id="update-sslTrust"
                  className="form-control"
                  value={sslTrust}
                  onChange={(e) => setSslTrust(e.target.value)}
                  required
                />
              </div>
              <div className="mb-3">
                <div className="form-check form-switch">
                  <input
                    type="checkbox"
                    id="update-auth"
                    className="form-check-input"
                    checked={auth}
                    onChange={(e) => setAuth(e.target.checked)}
                  />
                  <label htmlFor="update-auth" className="form-check-label">Authentication</label>
                </div>
              </div>
              <div className="mb-3">
                <div className="form-check form-switch">
                  <input
                    type="checkbox"
                    id="update-starttls"
                    className="form-check-input"
                    checked={starttls}
                    onChange={(e) => setStarttls(e.target.checked)}
                  />
                  <label htmlFor="update-starttls" className="form-check-label">STARTTLS</label>
                </div>
              </div>
              <div className="full-width" style={{ marginTop: "20px", textAlign: "right" }}>
                <button
                  type="button"
                  className="btn btn-secondary me-2"
                  onClick={() => setShowUpdateFields(false)}
                  style={{ marginRight: "10px", padding: "10px 15px", borderRadius: "8px" }}
                >
                  Cancel
                </button>
                <button 
                  type="submit" 
                  className="update-form-btn" 
                  disabled={isLoading}
                >
                  {isLoading ? 'Saving...' : 'Save Changes'}
                </button>
              </div>
            </form>
          </div>
        </>
      )}
    </div>
  );
};

export default Smtpaccounts;