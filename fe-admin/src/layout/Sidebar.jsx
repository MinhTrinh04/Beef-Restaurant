import { NavLink } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import {
    MdDashboard,
    MdRestaurantMenu,
    MdReceiptLong,
    MdPeople,
    MdLogout,
    MdShoppingCart
} from 'react-icons/md';

const Sidebar = () => {
    const { logout, user } = useAuth();

    const handleLogout = async () => {
        try {
            await logout();
        } catch (error) {
            console.error('Logout error:', error);
        }
    };

    return (
        <aside style={{
            width: '250px',
            backgroundColor: 'var(--bg-secondary)',
            borderRight: '1px solid var(--border)',
            display: 'flex',
            flexDirection: 'column',
            position: 'fixed',
            height: '100vh',
            left: 0,
            top: 0,
        }}>
            <div style={{ padding: '1.5rem', borderBottom: '1px solid var(--border)' }}>
                <h1 style={{ fontSize: '1.25rem', fontWeight: 700 }}>Beef Admin</h1>
                {user && (
                    <p style={{
                        fontSize: '0.875rem',
                        color: 'var(--text-secondary)',
                        marginTop: '0.5rem'
                    }}>
                        {user.email}
                    </p>
                )}
            </div>

            <nav style={{ flex: 1, padding: '1rem' }}>
                <NavLink to="/" end className={({ isActive }) =>
                    `nav-link ${isActive ? 'active' : ''}`
                }>
                    <MdDashboard size={20} />
                    <span>Dashboard</span>
                </NavLink>

                <NavLink to="/menu" className={({ isActive }) =>
                    `nav-link ${isActive ? 'active' : ''}`
                }>
                    <MdRestaurantMenu size={20} />
                    <span>Menu Management</span>
                </NavLink>

                <NavLink to="/orders" className={({ isActive }) =>
                    `nav-link ${isActive ? 'active' : ''}`
                }>
                    <MdShoppingCart size={20} />
                    <span>Orders</span>
                </NavLink>

                <NavLink to="/users" className={({ isActive }) =>
                    `nav-link ${isActive ? 'active' : ''}`
                }>
                    <MdPeople size={20} />
                    <span>Users</span>
                </NavLink>
            </nav>

            <div style={{ padding: '1rem', borderTop: '1px solid var(--border)' }}>
                <button
                    onClick={handleLogout}
                    className="btn btn-secondary"
                    style={{ width: '100%', justifyContent: 'center' }}
                >
                    <MdLogout size={20} />
                    <span>Logout</span>
                </button>
            </div>
        </aside>
    );
};

export default Sidebar;
