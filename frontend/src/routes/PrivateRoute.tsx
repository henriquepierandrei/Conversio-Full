import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import React from "react";

const PrivateRoute = () => {
  const { isAuthenticated, isLoading } = useAuth();

  console.log("🛡️ PrivateRoute -> isAuthenticated:", isAuthenticated);
  console.log("🕒 PrivateRoute -> isLoading:", isLoading);

  if (isLoading) {
    console.log("⏳ Carregando autenticação...");
    return <div>Carregando...</div>;
  }

  if (!isAuthenticated) {
    console.log("🚫 Usuário não autenticado, redirecionando para o login.");
    return <Navigate to="/login" replace />;
  }

  console.log("✅ Usuário autenticado, renderizando página.");
  return <Outlet />;
};

export default PrivateRoute;