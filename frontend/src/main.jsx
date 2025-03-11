import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import App from "./App.jsx";
import { AuthProvider } from "./context/AuthContext";

// Envolva o App com o BrowserRouter no nível mais alto
ReactDOM.createRoot(document.getElementById("root")).render(

    <AuthProvider>
      <BrowserRouter>
        <App />
      </BrowserRouter>
    </AuthProvider>

);
