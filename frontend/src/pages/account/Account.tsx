import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../account/Account.css';
import Header from '../../components/header/Header';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPencil, faSave, faTimes } from '@fortawesome/free-solid-svg-icons';
import Cookies from 'js-cookie';

function Account() {
  // Estado para armazenar os dados do usuário
  const [user, setUser] = useState({
    companyName: '',
    email: '',
    createdAt: '',
    accountId: ''
  });

  

  // Estado para controlar a edição dos campos
  const [isEditing, setIsEditing] = useState({
    companyName: false
  });

  // Estado para armazenar a mensagem de erro ou sucesso
  const [message, setMessage] = useState({ text: '', type: '' });

  // Buscar os dados do usuário ao carregar a página
  const fetchUserData = async () => {
    const token = Cookies.get('accessToken');
    if (!token) {
      alert('Usuário não autenticado. Faça login novamente.');
      return;
    }

    try {
      const response = await axios.get('http://localhost:8080/api/v1/user/get', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setUser({
        companyName: response.data.companyName,
        email: response.data.email,
        createdAt: response.data.createdAt,
        accountId: response.data.accountId
      });
    } catch (err) {
      console.error('Erro ao buscar dados do usuário:', err);
      setMessage({ text: 'Erro ao buscar os dados do usuário.', type: 'error' });
    }
  };

  useEffect(() => {
    fetchUserData();
  }, []);

  // Função para permitir a edição de um campo específico
  const handleEdit = (field: string) => {
    setIsEditing((prevState) => ({
      ...prevState,
      [field]: true
    }));
  };

  // Função para salvar as alterações
  const handleSave = async () => {
    const token = Cookies.get('accessToken');
    if (!token) {
      alert('Usuário não autenticado. Faça login novamente.');
      return;
    }

    try {
      await axios.put(
        `http://localhost:8080/api/v1/user/update/company-name`,
        { companyName: user.companyName },
        {
          params: { companyName: user.companyName },
          headers: { Authorization: `Bearer ${token}` }
        }
      );
      setIsEditing({ companyName: false });
      setMessage({ text: 'Nome da empresa atualizado com sucesso!', type: 'success' });
    } catch (err) {
      console.error('Erro ao salvar alterações:', err);
      setMessage({ text: 'Erro ao salvar as alterações.', type: 'error' });
    }
  };

  // Função para fechar a mensagem
  const closeMessage = () => {
    setMessage({ text: '', type: '' });
  };

  return (
    <div className='container-user-principal'>
      <div><Header /></div>
      <div className='container-user'>
        <div className="profile-settings">
          <h1>Conta</h1>
          <p>Altere os seus dados como preferir.</p>

          {/* Mensagem de sucesso ou erro */}
          {message.text && (
            <div className={`message ${message.type}`}>
              {message.text}
              <button className="close-button" onClick={closeMessage} style={{marginLeft: "15px"}}>
                <FontAwesomeIcon icon={faTimes} />
              </button>
            </div>
          )}

          <div className="image-upload">
            <img src="https://www.w3schools.com/w3images/avatar2.png" alt="profile" className="profile-image" />
            <p>Image must be 256×256 - Max 2MB</p>
            <div className="image-actions">
              <button className="upload-button">Upload Image</button>
              <button className="delete-button">Delete Image</button>
            </div>
          </div>

          <div className="form-grid">
            <div className="form-group">
              <label>Nome da Empresa</label>
              <div className="input-with-icon">
                <input
                  type="text"
                  value={user.companyName}
                  onChange={(e) => setUser({ ...user, companyName: e.target.value })}
                  disabled={!isEditing.companyName}
                />
                <button className='update-button-data-profile' onClick={() => handleEdit('companyName')}>
                  <FontAwesomeIcon icon={faPencil} />
                </button>
              </div>
            </div>

            <div className="form-group">
              <label>Email</label>
              <div className="input-with-icon">
                <input
                  type="email"
                  value={user.email}
                  disabled
                />
              </div>
            </div>

            <div className="form-group">
              <label>Criado em</label>
              <div className="input-with-icon">
                <input type="text" value={user.createdAt} style={{color:"gray"}}/>
              </div>
            </div>

            <div className="form-group">
              <label>ID da Conta</label>
              <div className="input-with-icon">
                <input
                  type="email"
                  value={user.accountId}
                  disabled
                  style={{color:"gray"}}
                />
              </div>
            </div>
          </div>

          <div className="form-actions">
            <button className="cancel-button" onClick={() => setIsEditing({ companyName: false })}>
              Cancel
            </button>
            <button className="save-button" onClick={handleSave}>
              <FontAwesomeIcon icon={faSave} /> Salvar
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Account;
