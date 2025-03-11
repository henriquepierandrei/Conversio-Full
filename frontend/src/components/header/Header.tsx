import React, { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUsersRectangle, faPlusCircle, faMinusCircle, faRotate, faSearch, faMailBulk, faPaperPlane, faBorderTopLeft, faTimeline, faCalendar, faGears, faTrash, faChevronDown, faChevronUp, faUserPlus, faUserMinus, faUserGear, faAddressCard, faEdit, faHeader, faDownLong, faSquare, faX, faUser, faGear, faUserEdit, faBookOpen, faHistory, faUsersBetweenLines, faUsersViewfinder, faPersonChalkboard, faUsersGear, faDashboard, faBars, faSignOutAlt, faHome, faEnvelope, faUsers } from '@fortawesome/free-solid-svg-icons';
import { useAuth } from "../../context/AuthContext";

import '../header/Header.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import logo from '../../assets/images/logos/Logo.png';

function Header() {
  const { logout } = useAuth();
  const [isMailOpen, setIsMailOpen] = useState(false);
  const [activeItem, setActiveItem] = useState(() => {
    const path = window.location.pathname;
    if (path.includes('dashboard')) return 'dashboard';
    if (path.includes('conta')) return 'conta';
    if (path.includes('enviar') || path.includes('temas') || path.includes('logs')) return 'email';
    if (path.includes('clientes')) return 'clientes';
    if (path.includes('contas-smtp')) return 'smtp';
    return '';
  });

  const toggleMailDropdown = () => setIsMailOpen(!isMailOpen);

  useEffect(() => {
    import('bootstrap/dist/js/bootstrap.bundle.min.js');
  }, []);

  const handleLogout = () => {
    if (window.confirm('Tem certeza que deseja sair?')) {
      logout();
    }
  };

  return (
    <div className="sidebar-container">
      <button className="btn-canvas" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasExample" aria-controls="offcanvasExample">
        <span className="menu-icon"><FontAwesomeIcon icon={faBars} /></span>
      </button>
      
      <div className="offcanvas offcanvas-start sidebar" tabIndex={-1} id="offcanvasExample" aria-labelledby="offcanvasExampleLabel">
        <div className="sidebar-header">
          <div className="logo-container">
            <img src={logo} alt="Conversio Logo" className="logo-image" />
          </div>
          <button type="button" className="close-button" data-bs-dismiss="offcanvas" aria-label="Close">
            <FontAwesomeIcon icon={faX} />
          </button>
        </div>
        

        <div className="sidebar-divider">Menu Principal</div>
        
        <nav className="sidebar-nav">
          <a href="/dashboard" className={`nav-item ${activeItem === 'dashboard' ? 'active' : ''}`} onClick={() => setActiveItem('dashboard')}>
            <FontAwesomeIcon icon={faHome} className="nav-icon" />
            <span className="nav-text">Dashboard</span>
          </a>
          
          <a href="/conta" className={`nav-item ${activeItem === 'conta' ? 'active' : ''}`} onClick={() => setActiveItem('conta')}>
            <FontAwesomeIcon icon={faUser} className="nav-icon" />
            <span className="nav-text">Minha Conta</span>
          </a>
          
          <div className={`nav-dropdown ${isMailOpen ? 'open' : ''}`}>
            <button className={`nav-item dropdown-toggle ${activeItem === 'email' ? 'active' : ''}`} onClick={toggleMailDropdown}>
              <FontAwesomeIcon icon={faEnvelope} className="nav-icon" />
              <span className="nav-text">Email</span>
              <FontAwesomeIcon icon={isMailOpen ? faChevronUp : faChevronDown} className="dropdown-icon" />
            </button>
            
            <div className={`dropdown-menu ${isMailOpen ? 'show' : ''}`}>
              <a href="/enviar" className="dropdown-item" onClick={() => setActiveItem('email')}>
                <FontAwesomeIcon icon={faPaperPlane} className="dropdown-icon" />
                <span>Enviar</span>
              </a>
              <a href="/temas" className="dropdown-item" onClick={() => setActiveItem('email')}>
                <FontAwesomeIcon icon={faBorderTopLeft} className="dropdown-icon" />
                <span>Temas</span>
              </a>
              <a href="/logs" className="dropdown-item" onClick={() => setActiveItem('email')}>
                <FontAwesomeIcon icon={faHistory} className="dropdown-icon" />
                <span>Logs</span>
              </a>
            </div>
          </div>
          
          <a href="/clientes" className={`nav-item ${activeItem === 'clientes' ? 'active' : ''}`} onClick={() => setActiveItem('clientes')}>
            <FontAwesomeIcon icon={faUsers} className="nav-icon" />
            <span className="nav-text">Clientes</span>
          </a>
          
          <a href="/contas-smtp" className={`nav-item ${activeItem === 'smtp' ? 'active' : ''}`} onClick={() => setActiveItem('smtp')}>
            <FontAwesomeIcon icon={faUsersGear} className="nav-icon" />
            <span className="nav-text">Contas SMTP</span>
          </a>
        </nav>
        
        <div className="sidebar-footer">
          <button className="logout-button" onClick={handleLogout}>
            <FontAwesomeIcon icon={faSignOutAlt} className="logout-icon" />
            <span>Sair</span>
          </button>
          
          <p className="copyright">Conversio - 2025</p>
        </div>
      </div>
    </div>
  );
}

export default Header;