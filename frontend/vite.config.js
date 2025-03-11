import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import path from 'path'; // Para usar os caminhos absolutos

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      'shared/libs': path.resolve(__dirname, 'src/shared/libs'), // Configura o alias
    },
  },
  
});
