import React, { useState, useEffect } from 'react';
import '../clients/Clients.css';
import Header from '../../components/header/Header';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowLeft, faArrowRight, faCheck, faX } from '@fortawesome/free-solid-svg-icons';
import axios from 'axios';
import Cookies from 'js-cookie';


interface Client {
  name: string;
  email: string;
  createdAt: string;
}

const Clients: React.FC = () => {
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [fileName, setFileName] = useState<string | null>(null);
  const [clients, setClients] = useState<Client[]>([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);

  const handleUploadCsv = async (e: React.FormEvent) => {
    e.preventDefault();
    const token = Cookies.get("accessToken");

    if (!token) {
      setError("Usuário não autenticado. Faça login novamente.");
      return;
    }

    const fileInput = document.getElementById("csvFileInput") as HTMLInputElement;
    const file = fileInput?.files?.[0];

    if (!file) {
      setError("Nenhum arquivo CSV selecionado.");
      return;
    }

    setFileName(file.name);

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await axios.post(
        "http://localhost:8080/api/v1/clients/add/csv",
        formData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "multipart/form-data",
          },
        }
      );

      setSuccess(`Arquivo enviado com sucesso! Código de status: ${response.status}`);
      console.log("Resposta do servidor:", response.data);

      fileInput.value = "";
      setFileName(null);
    } catch (err) {
      console.error("Erro ao enviar arquivo CSV:", err.response?.data || err.message);
      setError(`Falha ao enviar arquivo CSV: ${err.response?.data?.message || err.message}`);
    }
  };

  const fetchClients = async (pageNumber = 0) => {
    const token = Cookies.get("accessToken");

    if (!token) {
      setError("Usuário não autenticado. Faça login novamente.");
      return;
    }

    try {
      const response = await axios.get(
        `http://localhost:8080/api/v1/clients/get/all?page=${pageNumber}&size=10&sort=createdAt,DESC`,
        { headers: { Authorization: `Bearer ${token}` } }
      );

      if (response.status === 204) {
        setClients([]); // Nenhum cliente encontrado
      } else {
        setClients(response.data.content);
        setTotalPages(response.data.totalPages);
      }
      
    } catch (err) {
      setError("Falha ao buscar clientes: " + (err.response?.data?.message || err.message));
    }
  };

  useEffect(() => {
    fetchClients(page);
  }, [page]);

  // Limpar mensagens de erro e sucesso após 3 segundos
  useEffect(() => {
    const errorTimeout = setTimeout(() => {
      if (error) {
        setError(null);
      }
    }, 3000);

    const successTimeout = setTimeout(() => {
      if (success) {
        setSuccess(null);
      }
    }, 3000);

    return () => {
      clearTimeout(errorTimeout);
      clearTimeout(successTimeout);
    };
  }, [error, success]);

  const handlePageChange = (newPage: number) => {
    if (newPage >= 0 && newPage < totalPages) {
      setPage(newPage);
    }
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString("pt-BR");
  };




  return (
    <div className='container-client-principal'>
      <div style={{ position: "absolute", top: "5px", left: "5px" }}><Header /></div>
      <div className='container-client'>
        <h1 className='title-container-client'>Adicione Clientes</h1><br />
        <form onSubmit={handleUploadCsv}>
          <label className="custum-file-upload" htmlFor="csvFileInput">
            <div className="icon"></div>
            <div className="text">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                height="80"
                fill="currentColor"
              >
                {/* Ícone SVG */}
              </svg>
              <img src="https://cdn3.iconfinder.com/data/icons/ikooni-outline-file-formats/128/files2-19-512.png" alt="" width={"20%"} />
              <span>Clique aqui para enviar o Arquivo.CSV</span>
            </div>
            <input
              type="file"
              id="csvFileInput"
              name="file"
              accept=".csv"
              required
              onChange={(e) => {
                const file = e.target.files?.[0];
                if (file) {
                  setFileName(file.name); // Atualiza o nome do arquivo selecionado
                }
              }}
            />
          </label>
          <br />
          {/* Exibe o nome do arquivo selecionado */}
          {fileName && <p>Arquivo selecionado: <strong>{fileName}</strong></p>}
          <button type="submit" className='btn btn-add-client'>
            Adicionar Clientes
          </button>
        </form>
        {/* Exibe mensagens de sucesso ou erro */}
        {success && <div className='alert alert-success'><FontAwesomeIcon icon={faCheck} /> {success}</div>}
        {error && <div className='alert alert-danger'><FontAwesomeIcon icon={faX} /> {error}</div>}
      </div>

      {/* Tabela de clientes */}
      <div className='container-client-two'>
        <div style={{ overflow: "auto", width: "90%", textAlign: "start", borderRadius: "0.5em" }}>
          <table className="table table-striped tb-clients">
            <thead>
              <tr>
                <th scope="col" >Nome</th>
                <th scope="col" style={{ width: "0%" }}>Criado Em:</th>
                <th scope="col" style={{ width: "0%" }}>Email</th>
              </tr>
            </thead>
            <tbody>
              {clients.length > 0 ? (
                clients.map((client) => (
                  <tr key={client.email}>
                    <td style={{ width: "0%" }}>{client.name}</td>
                    <td style={{ width: "0%" }}>
                      {formatDate(new Date(client.createdAt).toLocaleDateString())}
                    </td>
                    <td style={{ display: "flexbox", justifyContent: "space-between", width: "max-content" }}>
                      {client.email}
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan={3} style={{ textAlign: 'center' }}>
                    Nenhum cliente encontrado.
                  </td>
                </tr>
              )}
            </tbody>

          </table>
        </div>
        <div style={{ display: "flex", width: "100%", justifyContent: "center" }}>
          <nav aria-label="Page navigation">
            <ul className="pagination">
              <li className={`page-item ${page === 0 ? "disabled" : ""}`}>
                <button className="page-link" onClick={() => handlePageChange(page - 1)}>
                  <FontAwesomeIcon icon={faArrowLeft} />
                </button>
              </li>
              {Array.from({ length: totalPages }, (_, index) => (
                <li key={index} className={`page-item ${index === page ? "active" : ""}`}>
                  <button className="page-link" onClick={() => handlePageChange(index)}>
                    {index + 1}
                  </button>
                </li>
              ))}
              <li className={`page-item ${page === totalPages - 1 ? "disabled" : ""}`}>
                <button className="page-link" onClick={() => handlePageChange(page + 1)}>
                  <FontAwesomeIcon icon={faArrowRight} />
                </button>
              </li>
            </ul>
          </nav>
        </div>

      </div>
    </div>
  );
};

export default Clients;