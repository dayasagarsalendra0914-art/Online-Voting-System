import { useEffect, useState } from 'react'
import './App.css'

const API = 'https://online-votingbackend.onrender.com'

function App() {
const [candidates, setCandidates] = useState([])
const [voterIds, setVoterIds] = useState({})
const [message, setMessage] = useState('Connecting to voting server...')
const [loading, setLoading] = useState(true)

async function loadCandidates() {
try {
const response = await fetch(API)
if (!response.ok) throw new Error('Server returned an error')
const html = await response.text()

  // The backend serves an HTML page, so use its existing page directly
  if (html.includes('Candidate List')) {
    window.location.href = API
    return
  }

  throw new Error('Could not load the voting page')
} catch {
  setMessage('The voting server is unavailable. Please try again in a moment.')
} finally {
  setLoading(false)
}

}

useEffect(() => {
loadCandidates()
}, [])

return (
<main style={{ fontFamily: 'Arial, sans-serif', maxWidth: 900, margin: '40px auto', padding: 20 }}>
<h1 style={{ color: '#205ec9' }}>Online Voting System</h1>
<p>Vote securely using your registered voter ID.</p>
{loading && <p>Connecting to the voting server… This may take up to a minute.</p>}
{!loading && (
<>
<p>{message}</p>
<p>
<a href={API} style={{ display: 'inline-block', background: '#205ec9', color: 'white', padding: '12px 18px', borderRadius: 8, textDecoration: 'none' }}>
Open Voting Page
</a>
</p>
</>
)}
</main>
)
}

export default App
