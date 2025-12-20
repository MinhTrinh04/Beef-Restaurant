import { NavLink } from 'react-router-dom';
import {
    MdDashboard,
    MdRestaurantMenu,
    MdReceiptLong,
    MdPeople,
    MdLogout
} from 'react-icons/md';

const Sidebar = () => {
    const menuItems = [
        { name: 'Dashboard', path: '/', icon: <MdDashboard size={20} /> },
        { name: 'Menu Management', path: '/menu', icon: <MdRestaurantMenu size={20} /> },
        { name: 'Orders', path: '/orders', icon: <MdReceiptLong size={20} /> },
        { name: 'Users', path: '/users', icon: <MdPeople size={20} /> },
    ];

    return (
        <aside style={{
            width: '260px',
            height: '100vh',
            backgroundColor: 'var(--bg-secondary)',
            borderRight: '1px solid var(--border)',
            position: 'fixed',
            left: 0,
            top: 0,
            display: 'flex',
            flexDirection: 'column',
            padding: '1.5rem',
            zIndex: 100
        }}>
            <div style={{ marginBottom: '2rem', padding: '0 0.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                <div style={{ width: '32px', height: '32px', background: 'var(--primary)', borderRadius: '8px' }}></div>
                <h1 style={{ fontSize: '1.2rem', fontWeight: 700, color: 'var(--text-primary)' }}>Beef Admin</h1>
            </div>

            <nav style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
                {menuItems.map((item) => (
                    <NavLink
                        key={item.path}
                        to={item.path}
                        style={({ isActive }) => ({
                            display: 'flex',
                            alignItems: 'center',
                            gap: '0.75rem',
                            padding: '0.75rem 1rem',
                            borderRadius: 'var(--radius)',
                            color: isActive ? '#fff' : 'var(--text-secondary)',
                            backgroundColor: isActive ? 'var(--primary)' : 'transparent',
                            fontWeight: isActive ? 600 : 500,
                            transition: 'var(--transition)'
                        })}
                    >
                        {item.icon}
                        <span>{item.name}</span>
                    </NavLink>
                ))}
            </nav>

            <button className="btn btn-outline" style={{ justifyContent: 'center', borderColor: 'var(--border)', color: 'var(--text-muted)' }}>
                <MdLogout size={18} />
                <span>Logout</span>
            </button>
        </aside>
    );
};

export default Sidebar;
