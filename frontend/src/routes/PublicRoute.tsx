import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import React from "react";

const PublicRoute = () => {
  const { isAuthenticated } = useAuth();

  // Se o usuário estiver autenticado, redirecione para a página inicial
  if (isAuthenticated) {
    console.log("Usuário autenticado, redirecionando para a página inicial.");
    return <Navigate to="/dashboard" replace />;
  }

  // Se o usuário não estiver autenticado, permita o acesso à rota pública
  return <Outlet />;
};

export default PublicRoute;