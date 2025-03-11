import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import Dashboard from "./pages/home/Dashboard";
import Login from "./pages/auth/Login";
import Register from "./pages/auth/Register";
import Clients from "./pages/clients/Clients";
import Smtpaccounts from "./pages/smtp-accounts/Smtpaccounts";
import Themes from "./pages/emails/Themes";
import Account from "./pages/account/Account";
import PrivateRoute from "./routes/PrivateRoute";
import PublicRoute from "./routes/PublicRoute";
import { useAuth } from "./context/AuthContext"; // Importe o hook useAuth
import SendEmail from "./pages/SendEmail/SendEmail";
import LogsPage from "./pages/logs/LogsPage";

function App() {
  const { isAuthenticated } = useAuth(); // Obtenha o estado de autenticação

  return (
    <div>
      <Routes>
        {/* Rotas públicas */}
        <Route element={<PublicRoute />}>
          <Route path="/login" element={<Login />} />
          <Route path="/registrar" element={<Register />} />
        </Route>

        {/* Rotas privadas */}
        <Route element={<PrivateRoute />}>
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/clientes" element={<Clients />} />
          <Route path="/contas-smtp" element={<Smtpaccounts />} />
          <Route path="/temas" element={<Themes />} />
          <Route path="/conta" element={<Account />} />
          <Route path="/enviar" element={<SendEmail />} />\
          <Route path="/logs" element={<LogsPage />} />

        </Route>

        {/* Rota padrão */}
        <Route
          path="*"
          element={
            isAuthenticated ? (
              <Navigate to="/dashboard" replace />
            ) : (
              <Navigate to="/login" replace />
            )
          }
        />
      </Routes>
    </div>
  );
}

export default App;