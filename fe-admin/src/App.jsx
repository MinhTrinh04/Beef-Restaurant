import { Routes, Route } from 'react-router-dom';
import Layout from './layout/Layout';
import Dashboard from './pages/Dashboard';
import Menu from './pages/Menu';
import Orders from './pages/Orders';
import Users from './pages/Users';
import Login from './pages/Login';
import ProtectedRoute from './auth/ProtectedRoute';

function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/" element={
        <ProtectedRoute>
          <Layout />
        </ProtectedRoute>
      }>
        <Route index element={<Dashboard />} />
        <Route path="menu" element={<Menu />} />
        <Route path="orders" element={<Orders />} />
        <Route path="users" element={<Users />} />
      </Route>
    </Routes>
  );
}

export default App;
