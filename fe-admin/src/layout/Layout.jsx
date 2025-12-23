import { Outlet } from 'react-router-dom';
import Sidebar from './Sidebar';

const Layout = () => {
    return (
        <div style={{ display: 'flex', minHeight: '100vh' }}>
            <Sidebar />
            <main style={{
                flex: 1,
                marginLeft: '260px',
                padding: '2rem',
                backgroundColor: 'var(--bg-primary)',
                color: 'var(--text-primary)'
            }}>
                <div className="container">
                    <Outlet />
                </div>
            </main>
        </div>
    );
};

export default Layout;
